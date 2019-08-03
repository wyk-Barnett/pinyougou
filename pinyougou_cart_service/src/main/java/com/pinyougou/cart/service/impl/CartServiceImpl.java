package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyangkun
 * @date 2019/8/2 0002 14:43
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Cart> addGoddsToCartList(List<Cart> cartList, Long itemId, Integer num) {
        //1.根据skuId查询商品明细sku的对象
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item == null){
            throw new RuntimeException("商品不存在");
        }
        if (!item.getStatus().equals("1")){
            throw new RuntimeException("操作不合法");
        }
        //2.根据sku对象得到商家id
        String sellerId = item.getSellerId();
        //3.根据商家id在购物车列表中查询购物车对象
        Cart cart = searchCartBySellerId(cartList, sellerId);
        if (cart==null){
            //4.如果购物车列表中不存在该商家的购物车,创造一个新的购物车对象
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            List<TbOrderItem> orderItemList = new ArrayList<>();
            TbOrderItem orderItem = createOrderItem(item, num);
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            //将新的购物车对象添加到购物车列表
            cartList.add(cart);
        }else {
            //5.如果购物车中存在该商家的购物车
            //判断该商品是否在该购物车的明细列表中
            TbOrderItem orderItem = searchOrderItem(cart.getOrderItemList(), itemId);
            //5.1如果不存在,创建新的购物车明细对象,并添加到该购物车的明细列表中
            if (orderItem == null){
                orderItem = createOrderItem(item, num);
                cart.getOrderItemList().add(orderItem);
            }else {
                //5.2如果存在,在原有的数量上增加,并更新金额
                orderItem.setNum(orderItem.getNum()+num);
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));
                //当明细的数量小于等于0,移除此明细
                if (orderItem.getNum()<=0){
                    cart.getOrderItemList().remove(orderItem);
                }
                //当购物车的明细数量为,在购物车列表中移除此购物车
                if (cart.getOrderItemList().size()==0){
                    cartList.remove(cart);
                }
            }
        }
        return cartList;
    }

    /**
     * 从redis中提取购物车
     * @param username 用户名
     * @return 购物车列表
     */
    @Override
    public List<Cart> findCartListFromRedis(String username) {
        System.out.println("从redis中提取购物车");
        List<Cart>  cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if (cartList==null){
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    /**
     * 将购物车从如redis缓存中
     * @param username 用户名
     */
    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        redisTemplate.boundHashOps("cartList").put(username,cartList);
        System.out.println("将购物车存入redis缓存");
    }

    /**
     *  将cookie和redis中的购物车合并
     * @param cartList1 购物车1
     * @param cartList2 购物车2
     * @return 返回新的购物车列表
     */
    @Override
    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
        for (Cart cart : cartList2) {
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                cartList1 = addGoddsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
            }
        }
        System.out.println("合并购物车!!!");
        return cartList1;
    }


    /**
     * 根据skuId在购物车列表明细中查询对应商品sku是否存在
     * @param orderItemList 明细列表
     * @param itemId skuid
     * @return 返回 根据skuId在购物车列表明细中对应的明细对象
     */
    private TbOrderItem searchOrderItem(List<TbOrderItem> orderItemList,Long itemId){
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue()== itemId.longValue()){
                return orderItem;
            }
        }
        return null;
    }

    /**
     * 根据商家id在购物车列表中查询购物车对象
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }

    /**
     *构建购物车明细对象
     * @param item
     * @param num
     * @return
     */
    private TbOrderItem createOrderItem(TbItem item,Integer num){
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
        return orderItem;
    }
}
