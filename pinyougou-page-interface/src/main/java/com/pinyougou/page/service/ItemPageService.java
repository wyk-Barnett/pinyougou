package com.pinyougou.page.service;

/**
 * @author wangyangkun
 * @date 2019/7/26 0026 9:29
 */
public interface ItemPageService {
    /**
     * 生成商品详细页
     * @param goodsId 商品id
     * @return 返回生成是否完成
     */
    boolean getItemHtml(Long goodsId);

    /**
     * 删除商品详细页
     * @param goodsIds
     * @return
     */
    boolean deleteItemHtml(Long[] goodsIds);
}
