package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Goods;


/*
 *   时间：2022/5/26
 *
 *
 */
public interface GoodsService extends IService<Goods> {


    //分页查询
    String getGoodsList(String name,int pagenum,int pagesize);

    //根据id删除商品
    String deleteGoodsById(int id);


    //添加商品
    String addGoods(Goods goods);

    //根据id获取商品信息
    String getGoodsById(int id);

    //修改商品信息

    String updateGoods(Goods goods);


    //随机查询10条商品信息
    String select10Goods();

    //根据商品名字查询商品
    String selectGoodsByName(String goodsName);
}
