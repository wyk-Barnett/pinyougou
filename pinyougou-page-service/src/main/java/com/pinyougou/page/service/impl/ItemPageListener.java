package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * 监听类 用于生成静态网页
 * @author wangyangkun
 * @date 2019/7/29 0029 13:51
 */
@Component("itemPageListener")
public class ItemPageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();
            System.out.println("监听到消息:"+ids);
            for (Long goodsId : ids) {
                boolean b = itemPageService.getItemHtml(goodsId);
                System.out.println("生成静态页面"+b);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
