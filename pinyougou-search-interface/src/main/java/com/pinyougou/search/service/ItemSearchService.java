package com.pinyougou.search.service;

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
}
