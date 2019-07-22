package com.pinyougou.search.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyangkun
 * @date 2019/7/22 0022 20:45
 */
public class MyTest {

    @Test
    public void test1(){
        Map map = new HashMap();
        map.put("1","zhangsan");

        Map map2 = new HashMap();
        map2.put("2","lisi");
        map2.put("3","wangwu");

        System.out.println(map);
        System.out.println(map2);
        map.putAll(map2);
        System.out.println(map);

    }
}
