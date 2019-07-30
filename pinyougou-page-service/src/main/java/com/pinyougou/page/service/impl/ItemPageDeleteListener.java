package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @author wangyangkun
 * @date 2019/7/29 0029 14:17
 */
@Component("itemPageDeleteListener")
public class ItemPageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();
            System.out.println("监听到消息:"+ids);
            boolean b = itemPageService.deleteItemHtml(ids);
            System.out.println("删除商品详细页:"+b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
