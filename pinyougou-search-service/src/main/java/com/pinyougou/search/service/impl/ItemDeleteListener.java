package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangyangkun
 * @date 2019/7/29 0029 13:16
 */
@Component("itemDeleteListener")
public class ItemDeleteListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage)message;
        try {
            Long[] ids = (Long[])objectMessage.getObject();
            System.out.println("监听到消息:"+ids);
            itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
            System.out.println("执行索引库删除");
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
