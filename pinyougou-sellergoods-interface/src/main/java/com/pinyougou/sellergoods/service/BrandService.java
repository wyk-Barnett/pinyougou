package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;
import entity.Result;

import java.util.List;
import java.util.Map;

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
    TbBrand findOne(Long id);

    /**
     * 修改品牌功能
     * @param brand 品牌对象
     */
    void update(TbBrand brand);

    /**
     * 根据id删除
     * @param ids 批量删除
     */
    void delete(Long[] ids);

    /**
     * 条件查询并分页
     * @param brand 查询实体条件
     * @param currentPage 当前页
     * @param pageSize 每页显示条数
     * @return 分页对象
     */
    PageResult findPage(TbBrand brand,int currentPage, int pageSize);

    /**
     * 查询所有品牌封装为map集合形式
     * @return
     */
    List<Map> selectOptionList();
}
