package com.pinyougou.cart.service;

import com.pinyougou.pojogroup.Cart;

import java.util.List;

/**
 * @author wangyangkun
 * @date 2019/8/2 0002 14:37
 */
public interface CartService {
    /**
     * 添加商品列表到购物车
     * @param list 需要添加的购物车对象集合
     * @param itemId skuid
     * @param num 购买数量
     * @return 返回新的购物车列表
     */
    List<Cart> addGoddsToCartList(List<Cart> list,Long itemId,Integer num);

    /**
     * 从redis中提取购物车
     * @param username 用户名
     * @return 购物车列表
     */
    List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车列表从如redis缓存中
     * @param username 用户名
     *  @param cartList 购物车列表
     */
    void saveCartListToRedis(String username,List<Cart> cartList);

    /**
     * 将cookie和redis中的购物车合并
     * @param cartList1 购物车1
     * @param cartList2 购物车2
     * @return 返回合并后的购物车
     */
    List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);

}
