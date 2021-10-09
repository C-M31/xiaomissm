package com.bjpowernode.listener;

import com.bjpowernode.pojo.ProductType;
import com.bjpowernode.service.ProductTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;


//商品类型存在于全局作用域中，和spring注册用的时同一个监听器，所以不能用依赖注入。
@WebListener
public class ProductTypeListener implements ServletContextListener {
    @Override
    //初始化全局作用域
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //手工从spring容器中取出service对象
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ApplicationContext_*.xml");
        ProductTypeService productTypeService = (ProductTypeService) applicationContext.getBean("ProductTypeServiceImpl");
        List<ProductType> typeList =productTypeService.getType();
        //放入全局作用域中，供新增页面，修改页面，前台页面的查询功能使用
        servletContextEvent.getServletContext().setAttribute("typeList",typeList);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
