package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyangkun
 * @date 2019/8/5 0005 16:50
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private OrderService orderService;


    @RequestMapping("/createNative")
    public Map createNative(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        TbPayLog payLog = orderService.searchPayLogFromRedis(name);
        if (payLog!=null){
            return weixinPayService.createNative(payLog.getOutTradeNo(), String.valueOf(payLog.getTotalFee()));
        }else {
            return new HashMap();
        }
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Result result = null;
        long startTime = System.currentTimeMillis();
        while (true){
            Map<String, String> map = weixinPayService.queryPayStatus(out_trade_no);
            if (map==null){
                result = new Result(false, "支付出错");
            }
            if (map.get("trade_state").equals("SUCCESS")){
                result = new Result(true, "支付成功");
                //修改订单状态 和支付日志
                orderService.updateOrderStatus(out_trade_no,map.get("transaction_id"));
                break;
            }
            try {
                Thread.sleep(3000);
                System.out.println("检测订单状态");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long nowTime = System.currentTimeMillis();
            if (nowTime-startTime>=(5*60*1000)){
                result = new Result(false,"二维码超时");
                break;
            }
        }
        return result;
    }
}
