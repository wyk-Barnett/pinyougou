package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojogroup.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wangyangkun
 * @date 2019/8/2 0002 17:55
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        //获取当前登录人
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录人:"+name);
        //未登录时,匿名用户名
        String anonymousUser = "anonymousUser";
        //从cookie中提取购物车
        System.out.println("从cookie中提取购物车");
        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if ("".equals(cartListString) || cartListString==null){
            cartListString="[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);

        if (anonymousUser.equals(name)){
            //未登录
            return cartList_cookie;
        }else {
            //已登录,从reids缓存中获取购物车
            System.out.println("从reids缓存中获取购物车");
            List<Cart> cartList_redis = cartService.findCartListFromRedis(name);
            //如果cookie中有数据,则调用方法合并购物车列表
            if (cartList_cookie!=null){
                cartList_redis = cartService.mergeCartList(cartList_redis, cartList_cookie);
                //保存到redis
                cartService.saveCartListToRedis(name,cartList_redis);
                //删除cookie中保存的购物车数据
                CookieUtil.deleteCookie(request,response,"cartList");
            }
            return cartList_redis;
        }
    }

    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins = "http://localhost:9105",allowCredentials = "true")
    public Result addGoodsToCartList(Long itemId,Integer num ) {

        //获取当前登录人
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录人:"+name);
        //未登录时,匿名用户名
        String anonymousUser = "anonymousUser";
        try {
            //提取购物车
            List<Cart> cartList = findCartList();
            //调用服务方法操作购物车
            cartList = cartService.addGoddsToCartList(cartList, itemId, num);

            if (anonymousUser.equals(name)){
                //将新的购物车存入cookie
                System.out.println("将新的购物车存入cookie");
                CookieUtil.setCookie(request,response, "cartList",JSON.toJSONString(cartList),3600*24,"UTF-8");
            }else {
                //已登录
                cartService.saveCartListToRedis(name,cartList);
                System.out.println("向redis中存购物车列表");
            }
            return new Result(true,"存入购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"存入购物车失败");
        }

    }
}
