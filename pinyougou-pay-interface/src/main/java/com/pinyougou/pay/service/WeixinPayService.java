package com.pinyougou.pay.service;

import java.util.Map;

/**
 * @author wangyangkun
 * @date 2019/8/5 0005 16:22
 */
public interface WeixinPayService {
    /**
     * 创建二维码
     * @param out_trade_no 商户订单号
     * @param total_fee 金额
     * @return 将创建二维码需要的信息封装进map返回
     */
    Map<String,String> createNative(String out_trade_no,String total_fee);

    /**
     *  查询订单状态
     * @param out_trade_no 商户订单号
     * @return 返回状态结果
     */
    Map<String,String> queryPayStatus(String out_trade_no);
}
