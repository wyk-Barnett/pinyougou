package com.pinyougou.test;

import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbTypeTemplateMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangyangkun
 * @date 2019/7/8 0008 21:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class MyTest {

    @Autowired
    private TbBrandMapper brandMapper;
    @Autowired
    private TbTypeTemplateMapper tbTypeTemplateMapper;
    @Test
    public void test(){
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        List<Long> list = new ArrayList<>();
        list.add((long)1);
        list.add((long)2);
        list.add((long)3);
        criteria.andIdIn(list);
        List<TbBrand> brandList = brandMapper.selectByExample(example);
        System.out.println(brandList);
    }

    @Test
    public void test2(){
        List<Map> list = tbTypeTemplateMapper.findAllId();
        System.out.println(list);
    }

    @Autowired
    private TbSpecificationMapper specificationMapper;
    @Test
    public void test3(){
        List<Map> list = specificationMapper.selectOptionList();
        System.out.println(list);
    }
}
