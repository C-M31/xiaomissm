package com.bjpowernode.service;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductInfoService {
    //点击商品管理显示第一页，数据分页
    PageInfo<ProductInfo> split(int pageNum,int pageSize);


    //更新页面，添加数据
    int save(ProductInfo info );

    //编辑按钮，先按主键查询到这条数据
    ProductInfo getById(int pid);

    //编辑页面，更新这条数据
    int update(ProductInfo info);

    //删除按钮，按主键删除到这条数据
    int delete(int pid);

    //批量删除
    int deletebatch(String[] strings);

    //多条件查询
    List<ProductInfo> seleteCondition(ProductInfoVo vo);

    //多条件查询分页
    PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo,int pageSize);

}
