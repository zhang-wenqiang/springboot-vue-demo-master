package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Order;

/*
 *   时间：2022/5/26
 *
 *
 */
public interface OrderService extends IService<Order> {


//分页查询订单

    String getPageOrder(String name, int pagenum, int pagesize);


    //根据id修改地址


    String updateAddress(int id, String address);

    //删除订单
    String deleteOrder(int id);

    //添加订单
    String insertOrder(Order order);

    ////根据用户id查询未支付的订单
    String selectOrderNoPay(int id);

    //修改订单为已付款
    String updateIsPay(int id,int paymoney);

    //根据用户id查询订单
    String selectOrderByUserId(int userid);
}
