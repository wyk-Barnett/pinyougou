package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

/**
 * @author wangyangkun
 * @date 2019/7/21 0021 18:46
 */
public interface ItemSearchService {
    /**
     * 搜索方法
     * @param map 多元参数 使用map接收
     * @return
     */
    Map search(Map map);

    /**
     * 往solr索引库中更新数据
     * @param list
     */
    void importList(List list);

    /**
     * 删除索引库中数据
     * @param list 根据goodsid集合
     */
    void deleteByGoodsIds(List list);
}
