package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;
import entity.Result;

import java.util.List;

/**
 * 品牌 接口
 * @author wangyangkun
 * @date 2019/7/8 0008 19:36
 */
public interface BrandService {
    /**
     * 查询所有品牌
     * @return 品牌集合
     */
    List<TbBrand> findAll();

    /**
     * 分页查询品跑
     * @param currentPage 当前页
     * @param pageSize 每页显示条数
     * @return 结果对象
     */
    PageResult findPage(int currentPage, int pageSize);

    /**
     * 新增产品
     * @param brand 产品对象
     */
    void add(TbBrand brand);

    /**
     * 根据id查询产品
     * @param id id
     * @return 返回尸体对象
     */
    TbBrand findOne(long id);





}
