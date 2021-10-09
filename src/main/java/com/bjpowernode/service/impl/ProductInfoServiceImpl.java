package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.AdminExample;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductInfoExample;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    ProductInfoMapper productInfoMapper;

    @Override
    public PageInfo<ProductInfo> split(int pageNum,int pageSize) {
        //先进行分页处理
        PageHelper.startPage(pageNum,pageSize);
        //条件分装
        ProductInfoExample example = new ProductInfoExample();
        example.setOrderByClause("p_id desc");

        List<ProductInfo> list = productInfoMapper.selectByExample(example);
        PageInfo<ProductInfo> info = new PageInfo<>(list);

        return info;
    }

    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getById(int pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deletebatch(String[] strings) {
        return productInfoMapper.deletebatch(strings);
    }

    @Override
    public List<ProductInfo> seleteCondition(ProductInfoVo vo) {
        return productInfoMapper.seleteCondition(vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo,int pageSize) {
        //先进行分页处理
        PageHelper.startPage(vo.getPage() ,pageSize);

        List<ProductInfo> list = productInfoMapper.seleteCondition(vo);
        PageInfo<ProductInfo> info = new PageInfo<>(list);
        return info;

    }


}
