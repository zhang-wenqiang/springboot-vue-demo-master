package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Order;
import com.example.demo.pojo.IsPayOrder;
import com.example.demo.pojo.MyCartOrder;
import com.example.demo.pojo.OrderPojo;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;


//由于order在MySQL中是一个关键词，所以要加``,但是baseMapper中的不带，需要自己写
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Update("update t_order set state = #{state},pay_time = #{payTime} where order_no = #{tradeNo}")
    int updateState(@Param("tradeNo") String tradeNo, @Param("state") int state, @Param("payTime") String payTime);


    //分页查询
    List<OrderPojo> getPageOrder3(@Param("name") String name, @Param("currentrow") int currentrow, @Param("pagesize") int pagesize );

    @Select("SELECT o.id,u.`name`,g.goods_name,o.address,o.create_time,o.update_time,o.paymoney,o.ispay FROM `order` o \n" +
            "JOIN `user` u \n" +
            "on o.userid=u.id \n" +
            "JOIN goods g\n" +
            "on o.goodsid=g.id\n" +
            "LIMIT #{currentrow},#{pagesize}")
    List<OrderPojo> getPageOrder2(@Param("currentrow") int currentrow,@Param("pagesize") int pagesize );

    //修改地址
    @Update("update `order` set address = #{address} where id = #{id}")
    int updateAddress(int id,String address);

    @Delete("delete from `order` where id = #{id}")
    int deleteOrder(int id);


    //添加订单
    @Insert("INSERT INTO `order`(`userid`, `goodsid`, `address`, `create_time`, `update_time`, `paymoney`, `ispay`)" +
            "VALUES ( #{userid}, #{goodsid}, #{address},\n" +
            " #{createTime}, #{updateTime}, #{paymoney}, #{ispay});")
    int insertOrder(Order order);

    //查询购物车
    @Select("select o.id orderid, g.* \n" +
            "from `goods` g JOIN `order` o \n" +
            "where o.userid=#{userid} \n" +
            "and o.ispay=0\n" +
            "and g.id = o.goodsid")
    List<MyCartOrder> selectList(int userid);

    //修改订单为已付款
    @Update("update `order` set paymoney=#{paymoney},ispay=1 where id = #{id}")
    int updateOrderIsPay(int id,int paymoney);

    //根据用户id查询订单
    @Select("SELECT o.id,u.`name`,g.goods_name,g.img,o.address,o.create_time,o.update_time,o.paymoney,o.ispay \n" +
            "FROM `order` o \n" +
            "JOIN `user` u \n" +
            "on o.userid=u.id \n" +
            "JOIN goods g\n" +
            "on o.goodsid=g.id\n" +
            "WHERE o.userid=#{userid}\n" +
            "and o.ispay=1")
    List<IsPayOrder> selectOrderByUserId(int userid);

    //根据用户id删除订单
    @Delete("delete from `order` where userid=#{userid}")
    int delete(int userid);

    //查询所有订单
    @Select("select * from `order`")
    List<Order> selectAllOder();

    //根据
    List<OrderPojo> getOrderByUserName(String name);
}
