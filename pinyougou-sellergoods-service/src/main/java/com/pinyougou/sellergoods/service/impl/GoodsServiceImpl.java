package com.pinyougou.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsExample;
import com.pinyougou.pojo.TbGoodsExample.Criteria;

import entity.PageResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbBrandMapper brandMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbSellerMapper sellerMapper;
	@Autowired
	private TbItemMapper itemMapper;

	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		//设置状态:未审核
		goods.getGoods().setAuditStatus("0");
		goodsMapper.insert(goods.getGoods());
		goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
		goodsDescMapper.insert(goods.getGoodsDesc());
		String isEnableSpec ="1";
		//判断是否启用规格
		if (isEnableSpec.equals(goods.getGoods().getIsEnableSpec())) {
			//启用规格
			for (TbItem item : goods.getItemList()) {
				//构建标题  SPU名称+规格选项值
				String title = goods.getGoods().getGoodsName();
				Map<String, Object> map = JSON.parseObject(item.getSpec(), Map.class);
				for (String key : map.keySet()) {
					title += " " + map.get(key);
				}
				item.setTitle(title);

				setItemValue(item,goods);
				//插入数据
				itemMapper.insert(item);
			}
		}else {
			//没有启用规格
			TbItem item = new TbItem();
			item.setTitle(goods.getGoods().getGoodsName());
			item.setPrice(goods.getGoods().getPrice());
			//库存
			item.setNum(99999);
			//状态
			item.setStatus("1");
			//默认
			item.setIsDefault("1");
			//规格
			item.setSpec("{}");
			setItemValue(item,goods);
			itemMapper.insert(item);
		}
	}

	/**
	 * 规格详情添加封装
	 * @param item item
	 * @param goods goods
	 */
	private void setItemValue(TbItem item,Goods goods){
		//商品分类  三级分类id
		item.setCategoryid(goods.getGoods().getCategory3Id());
		//创建日期
		item.setCreateTime(new Date());
		//更新日期
		item.setUpdateTime(new Date());
		//商品ID
		item.setGoodsId(goods.getGoods().getId());
		//商家ID
		item.setSellerId(goods.getGoods().getSellerId());
		//品牌名称
		item.setBrand(brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId()).getName());
		//分类名称
		item.setCategory(itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id()).getName());
		//商家名称
		item.setSeller(sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId()).getNickName());
		//图片信息
		String itemImages = goods.getGoodsDesc().getItemImages();
		List<Map> imageList = JSON.parseArray(itemImages, Map.class);
		if (imageList.size() > 0) {
			item.setImage((String) imageList.get(0).get("url"));
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		goodsMapper.updateByPrimaryKey(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setGoods(tbGoods);
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(tbGoodsDesc);
		return goods;

	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
