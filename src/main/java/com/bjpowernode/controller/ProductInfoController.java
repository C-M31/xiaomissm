package com.bjpowernode.controller;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoController {

    @Autowired
    ProductInfoService productInfoService;

    public static final int PAGE_SIZE = 5;

    String saveFileName = "";

    //分页第一页数据
    @RequestMapping("/split.action")
    public String splist(HttpServletRequest request) {
        PageInfo<ProductInfo> info =null;
        Object vo = request.getSession().getAttribute("prodvo");
        if(vo!=null){
            info = productInfoService.splitPageVo((ProductInfoVo) vo,PAGE_SIZE);
            request.getSession().removeAttribute("prodvo");
        }else{
            //得到第一页的数据
            info = productInfoService.split(1, PAGE_SIZE);
        }

        request.setAttribute("info", info);
        return "product";

    }

    //ajax分页数据，又实现多条件查询
    @RequestMapping("/ajaxsplit.action")
    @ResponseBody
    public void ajaxsplit(ProductInfoVo vo, HttpServletRequest request) {
        PageInfo<ProductInfo> info = productInfoService.splitPageVo(vo,PAGE_SIZE);

        request.getSession().setAttribute("info", info);
    }



    //异步ajax图片回显
    @RequestMapping("/ajaxImg.action")
    @ResponseBody
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {
        //将图片名称UUID: UUID+原文件名称后缀名.jsp.png
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //System.out.println(saveFileName);

        //得到项目中图片存储的位置，并转存到服务器中
        String path = request.getServletContext().getRealPath("/image_big");
        //System.out.println(path);
        try {
            //File.separator是个分隔符'/'
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回客户端json对象，封装图片的路径，为了在页面上回显
        JSONObject objec = new JSONObject();
        objec.put("imgurl", saveFileName);

        //返回的是json的数据格式
        return objec.toString();
    }


    //添加数据
    @RequestMapping("/save.action")
    public String save(ProductInfo info, HttpServletRequest request) {
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        //info 对象中有表单提交上来的5个数据，有异步ajax的图片名称数据，有上架时间数据
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "增加成功！");
        } else {
            request.setAttribute("msg", "增加失败！");
        }
        //清空saveFileName变量，为下次增加或修改的异步ajax的上传处理,互不干扰。
        saveFileName = "";
        //跳转到第一页显示数据
        return "forward:/prod/split.action";

    }

    //编辑按钮
    @RequestMapping("/one.action")
    public String one(int pid,ProductInfoVo vo, Model model,HttpSession session) {
        ProductInfo info = productInfoService.getById(pid);
        model.addAttribute("prod", info);
        //将多条件和页码放到session中，更新处理结束后分页时读取条件及页码进行处理
        session.setAttribute("prodvo",vo);
        return "update";
    }

    //编辑页面，点击提交按钮更新
    @RequestMapping("/update.action")
    public String update(ProductInfo info, HttpServletRequest request) {
        //判断图片是否有上传，如果有，saveFileName不为空。
        if (!saveFileName.equals("")) {
            info.setpImage(saveFileName);
        }
        //完成更新
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "更新成功！！！");
        } else {
            request.setAttribute("msg", "更新失败！！！");
        }
        //清空saveFileName变量，为下次增加或修改的异步ajax的上传处理,互不干扰。
        saveFileName = "";

        return "forward:/prod/split.action";
    }


    //删除按钮
    @RequestMapping("/delete.action")
    public String delete(int pid,ProductInfoVo vo, HttpServletRequest request) {
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(num>0){
            request.setAttribute("msg","删除成功！！！");
            request.getSession().setAttribute("deleteProdVo",vo);
        }else{
            request.setAttribute("msg","删除失败！！!");
        }
        return "forward:/prod/deleteAjaxSplit.action";

    }

    //删除跳转到这里，分页数据取得第一页数据，
    @RequestMapping(value = "/deleteAjaxSplit.action",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Object deleteAjaxSplit( HttpServletRequest request) {
        PageInfo<ProductInfo> info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if(vo!=null){
            info = productInfoService.splitPageVo((ProductInfoVo) vo,PAGE_SIZE);
        }else{
            //取得第一页数据
            info = productInfoService.split(1, PAGE_SIZE);
        }
        //必须放到session中，因为会刷新
        request.getSession().setAttribute("info", info);
        return request.getAttribute("msg");

    }

    //批量删除
    @RequestMapping("deletebatch.action")
    public String deletebatch(String str,ProductInfoVo vo,HttpServletRequest request){
        String[] strings = str.split(",");
        int num =-1;
        try {
            num = productInfoService.deletebatch(strings);
            if(num>0){
                request.setAttribute("msg","批量删除成功！！！");
                request.getSession().setAttribute("deleteProdVo",vo);
            }else {
                request.setAttribute("msg","批量删除失败！！！");
            }
        } catch (Exception e) {
            request.setAttribute("msg","商品不可删除！！！");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }


    //多条件查询，没有实现分页
    @RequestMapping("condition.action")
    @ResponseBody
    public void condition(ProductInfoVo vo, HttpSession session){
        List<ProductInfo> list = productInfoService.seleteCondition(vo);
        session.setAttribute("list",list);

    }
}
