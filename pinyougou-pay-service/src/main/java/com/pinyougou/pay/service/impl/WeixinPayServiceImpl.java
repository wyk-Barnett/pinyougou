package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import util.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyangkun
 * @date 2019/8/5 0005 16:25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WeixinPayServiceImpl implements WeixinPayService {
    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;

    /**
     * 创建二维码
     * @param out_trade_no 商户订单号
     * @param total_fee 金额
     * @return 将创建二维码需要的信息封装进map返回
     */
    @Override
    public Map<String, String> createNative(String out_trade_no, String total_fee) {
        //1.创建参数
        Map<String, String> param = new HashMap<>();
        param.put("appid",appid);
        param.put("mch_id",partner);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("body","品优购");
        param.put("out_trade_no", out_trade_no);
        param.put("total_fee", total_fee);
        param.put("spbill_create_ip", "127.0.0.1");
        param.put("notify_url","http://www.itcast.com");
        param.put("trade_type", "NATIVE");

        try {
            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("请求的参数"+paramXml);
            //2.发送请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(paramXml);
            client.post();

            //3.接收响应结果
            String result = client.getContent();
            System.out.println("返回的结果:"+result);
            Map<String, String> mapResult = WXPayUtil.xmlToMap(result);
            System.out.println("微信返回结果:"+mapResult);

            Map map = new HashMap();
            map.put("code_url", mapResult.get("code_url"));
            map.put("total_fee", total_fee);
            map.put("out_trade_no",out_trade_no);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {
        //1.封装参数
        Map<String, String> param = new HashMap<>(16);
        param.put("appid",appid);
        param.put("mch_id", partner);
        param.put("out_trade_no",out_trade_no);
        param.put("nonce_str", WXPayUtil.generateNonceStr());

        //2.发送请求
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();

            //3.接收响应结果
            String result = client.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            System.out.println(map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
