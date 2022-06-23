package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.service.impl.OrderServiceImpl;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("order")
@CrossOrigin
public class OrderController {

    @Autowired
    OrderMapper OrderMapper;

    @Autowired
    OrderServiceImpl orderService;

    //分页查询
    @GetMapping("getPageOrder")
    public String getPageOrder(String query, int pagenum, int pagesize) {
        return orderService.getPageOrder(query, pagenum, pagesize);
    }


    //根据id修改订单地址

    @PutMapping("/updateAddress")
    public String updateAddress(int id, String address) {
        return orderService.updateAddress(id, address);
    }


    //删除订单
    @DeleteMapping( "/deleteOrder")
    public String deleteOrder(int id) {

        return orderService.deleteOrder(id);
    }
    @DeleteMapping(value = "/deleteOrderX",produces = "application/json;charset=utf-8")
    public String deleteOrderX(int id) {
        System.out.println(id);
        return orderService.deleteOrder(id);
    }

    //添加订单
    @PostMapping("/insertOrder")
    public String insertOrder(Order order) {
        return orderService.insertOrder(order);
    }

    @PostMapping(value = "/insertOrderX", produces = "application/json;charset=utf-8")
    public String insertOrderX(Order order) {
        return orderService.insertOrder(order);
    }

    //根据用户id查询未支付的订单
    @GetMapping(value = "/getOrderNoPay",produces = "application/json;charset=utf-8")
    public String getOrderNoPay(int id) {
        return orderService.selectOrderNoPay(id);
    }


    //修改订单为已付款
    @PutMapping(value = "/updateIsPay",produces = "application/json;charset=utf-8")
    public String updateIsPay(int orderid,int paymoney){
        return orderService.updateIsPay(orderid,paymoney);
    }

    //根据用户id查询订单
    @GetMapping(value = "/getOrderByUserIdX",produces = "application/json;charset=utf-8")
    public String getOrderByUserIdX(int userid){
        return orderService.selectOrderByUserId(userid);
    }
}
