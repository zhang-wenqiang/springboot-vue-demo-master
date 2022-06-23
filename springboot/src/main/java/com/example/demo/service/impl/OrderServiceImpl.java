package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Result;
import com.example.demo.entity.Goods;
import com.example.demo.entity.Order;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.pojo.IsPayOrder;
import com.example.demo.pojo.MyCartOrder;
import com.example.demo.pojo.OrderPojo;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 *   时间：2022/5/26
 *
 *
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    GoodsMapper goodsMapper;

    private Lock lock = new ReentrantLock();

    //分页查询
    @Override
    public String getPageOrder(String name, int pagenum, int pagesize) {
        Map<String, Object> map = new HashMap<>();
        List<OrderPojo> pageOrder = new ArrayList<>();
        int total = 0;
        if (name.equals("")) {
            pageOrder = orderMapper.getPageOrder2((pagenum - 1) * pagesize, pagesize);
            List<Order> orders = orderMapper.selectAllOder();
            total = orders.size();
        } else {
            pageOrder = orderMapper.getPageOrder3(name, (pagenum - 1) * pagesize, pagesize);
            List<OrderPojo> orderByUserName = orderMapper.getOrderByUserName(name);
            total = orderByUserName.size();
        }
        map.put("orders", pageOrder);
        map.put("total", total);
        return JSON.toJSONString(map);
    }

    //根据id修改地址
    @Override
    public String updateAddress(int id, String address) {
        int i = orderMapper.updateAddress(id, address);
        if (i > 0) {
            return Result.successMsg("修改地址成功");
        }
        return Result.errMsg("无需修改");
    }

    //删除订单
    @Override
    public String deleteOrder(int id) {


        int i = orderMapper.deleteOrder(id);

        if (i > 0) {
            return Result.successMsg("删除成功");
        } else {
            return Result.errMsg("无需删除");
        }
    }

    //添加订单
    @Override
    public String insertOrder(Order order) {
        lock.lock();
        Goods i = goodsMapper.selectById(order.getGoodsid());
        if (i.getAmount() == 0) {
            return Result.errMsg("商品数量不足");
        }
        goodsMapper.updateGoodsAmount(order.getGoodsid());

        //System.out.println("到这了");
        int insert = orderMapper.insertOrder(order);
        lock.unlock();
        if (insert > 0) {
            return Result.successMsg("购买成功");
        } else {
            return Result.errMsg("无法购买");
        }
    }

    //根据用户id查询未支付的订单
    @Override
    public String selectOrderNoPay(int id) {

        List<MyCartOrder> orders = orderMapper.selectList(id);
        return JSON.toJSONString(orders);
    }

    //修改订单为已付款,参数为：订单id、商品价格
    @Override
    public String updateIsPay(int id, int paymoney) {

        int i = orderMapper.updateOrderIsPay(id, paymoney);
        if (i > 0) {
            return Result.success();
        } else {
            return Result.error();
        }

    }

    //根据用户id查询订单
    @Override
    public String selectOrderByUserId(int userid) {
        List<IsPayOrder> orderPojos = orderMapper.selectOrderByUserId(userid);
        return JSON.toJSONString(orderPojos);
    }
}
