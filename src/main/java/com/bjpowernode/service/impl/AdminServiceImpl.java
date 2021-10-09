package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.AdminMapper;
import com.bjpowernode.pojo.Admin;
import com.bjpowernode.pojo.AdminExample;
import com.bjpowernode.service.AdminService;
import com.bjpowernode.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
     AdminMapper adminMapper;
    @Override
    public Admin login(String name, String pwd) {
        //将name封装进条件
        AdminExample adminExample = new AdminExample();
        adminExample.createCriteria().andANameEqualTo(name);

        List<Admin> list = adminMapper.selectByExample(adminExample);
        if(list!=null&&list.size()>0){
            Admin admin = list.get(0);
            String s = MD5Util.getMD5(pwd);
            if(s.equals(admin.getaPass())){
                return admin;
            }
        }
        return null;
    }
}
