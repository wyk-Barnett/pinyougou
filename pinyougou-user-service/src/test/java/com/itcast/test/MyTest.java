package com.itcast.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyangkun
 * @date 2019/7/31 0031 14:03
 */
public class MyTest {

    @Test
    public void test1(){
        String smscode = "542364";
        Map map = new HashMap();
        map.put("number",smscode);
        System.out.println(JSON.toJSONString(map));

    }
}
