package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 品牌实现类
 * @author wangyangkun
 * @date 2019/7/8 0008 19:47
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<TbBrand> brands = brandMapper.selectByExample(null);
        PageInfo pageInfo = new PageInfo(brands);
        PageResult pr = new PageResult(pageInfo.getTotal(),pageInfo.getList());
        return pr;
    }

    @Override
    public void add(TbBrand brand) {
        brandMapper.insert(brand);
    }
}
