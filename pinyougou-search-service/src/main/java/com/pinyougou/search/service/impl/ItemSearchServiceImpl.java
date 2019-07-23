package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyangkun
 * @date 2019/7/21 0021 19:33
 */
@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 搜索方法
     * @param searchMap  {"keywords":"","category":"","brand":"",spec:{},price:""};
     * @return 返回搜索结果
     */
    @Override
    public Map search(Map searchMap) {
        Map map = new HashMap();
        //1.高亮显示 条件查询
        Map itemList = searchList(searchMap);
        //将高亮查询方法返回的map全部追加到最后返回的map集合中
        map.putAll(itemList);

        //2.分组查询 商品列表
        List<String> categryList = searchCategryList(searchMap);
        map.put("categryList",categryList);

        //3.查询品牌和规格列表
        String category = (String) searchMap.get("category");
        if (!"".equals(category)){
            Map brandAndSpecList = searchBrandAndSpecList(category);
            map.putAll(brandAndSpecList);
        }else{
            if (categryList.size()>0){
                Map brandAndSpecList = searchBrandAndSpecList(categryList.get(0));
                map.putAll(brandAndSpecList);
            }
        }
        return map;
    }

    /**
     * 查询品牌和规格列表
     * @param category 品牌分类名称
     * @return 返回封装了品牌和规格列表的map集合
     */
    private Map searchBrandAndSpecList(String category){
        Map map = new HashMap();
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (typeId!=null){
            List<Map> brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            map.put("brandList",brandList);
            List<Map> specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList",specList);
        }
        return map;
    }

    /**
     * 分组查询 查询商品列表
     * @param searchMap 参数
     * @return 分组列表集合
     */
    private List<String> searchCategryList(Map searchMap){
        List<String> list = new ArrayList();

        Query query = new SimpleQuery("*:*");
        //根据关键字查询
        Criteria criteria = new Criteria("item_keywords");
        criteria.is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //设置分组选项
        GroupOptions options = new GroupOptions();
        options.addGroupByField("item_category");
        query.setGroupOptions(options);
        //获取分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        //获取分组结果对象
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //获取分组入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        //获取分组入口集合
        List<GroupEntry<TbItem>> content = groupEntries.getContent();
        for (GroupEntry<TbItem> entry : content) {
            list.add(entry.getGroupValue());
        }
        return list;
    }

    /**
     * 条件查询 高亮
     * @param searchMap 查询条件
     * @return 返回map 便于扩充
     */
    private Map searchList(Map searchMap){
        Map map = new HashMap(16);
        //高亮选项初始化
        HighlightQuery query = new SimpleHighlightQuery();
        //设置需要高亮显示的field
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        //前缀 后缀
        highlightOptions.setSimplePrefix("<span style='color:red'>");
        highlightOptions.setSimplePostfix("</span>");
        //为查询对象设置为高亮选项
        query.setHighlightOptions(highlightOptions);

        //1.1 关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //1.2 按商品分类过滤
        String category = "category";
        if (!"".equals(searchMap.get(category))){
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get(category));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.3按品牌分类过滤
        String brand = "brand";
        if (!"".equals(searchMap.get(brand))){
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get(brand));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.4按规格过滤
        String spec = "spec";
        if (searchMap.get(spec) != null){
            Map<String,String> specMap = (Map<String,String>) searchMap.get(spec);
            for (String key:specMap.keySet()) {
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //1.5按价格过滤
        String price = "price";
        if (!"".equals(searchMap.get(price))){
            String priceStr = (String) searchMap.get(price);
            String[] prices = priceStr.split("-");
            //根据前端定义 最低价格为0  最高为*
            String lowPrice = "0";
            String highPrice = "*";
            //设置最低价格,如果传过来的是0 则最低价格没有限制
            if (!lowPrice.equals(prices[0])){
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            //设置最高价格,如果传过来的是* 则最高价格没有限制
            if (!highPrice.equals(prices[1])){
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //1.6 分页
        Integer pageNum = (Integer) searchMap.get("pageNum");
        if (pageNum==null){
            pageNum = 1;
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (pageSize == null){
            pageSize = 20;
        }
        query.setOffset((pageNum-1)*pageSize);
        query.setRows(pageSize);


        // *********************获取高亮结果集******************
        //高亮页对象
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //高亮入口集合
        List<HighlightEntry<TbItem>> highlighted = page.getHighlighted();
        for (HighlightEntry<TbItem> entry : highlighted) {
            List<HighlightEntry.Highlight> highlights = entry.getHighlights();

            if (highlights.size()>0 && highlights.get(0).getSnipplets().size()>0){
                TbItem item = entry.getEntity();
                //将高亮设置后的title赋值给item对象
                item.setTitle(highlights.get(0).getSnipplets().get(0));
            }
        }
        System.out.println(page.getContent());
        map.put("rows",page.getContent());
        map.put("totalPages",page.getTotalPages());
        map.put("total",page.getTotalElements());
        return map;
    }

}
