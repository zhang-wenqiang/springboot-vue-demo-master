package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Goods;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.service.impl.GoodsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
 *   时间：2022/5/25
 *
 *
 */
@RestController
@RequestMapping("/goods")
@CrossOrigin
public class GoodsController {


    @Autowired
    GoodsServiceImpl goodsService;

    @Autowired
    GoodsMapper goodsMapper;


    //商品分页查询,根据传入的商品名字
    @GetMapping("/getGoodsList")
    public String getGoodsList(String query,int pagenum,int pagesize){

        return goodsService.getGoodsList(query,pagenum,pagesize);
    }

    //根据传入的商品名字查询
    @GetMapping(value = "/getGoodsListX",produces = "application/json;charset=utf-8")
    public String getGoodsListX(String goodsName){

        return goodsService.selectGoodsByName(goodsName);
    }

    //根据id删除商品
    @DeleteMapping("/deleteGoods")
    public String deleteGoods(int id){


        return goodsService.deleteGoodsById(id);
    }

    //添加商品
    @PostMapping("/addGoods")
    public String addGoods(Goods goods){

        System.out.println(goods);
        return goodsService.addGoods(goods);
    }

    //根据id获取商品信息
    @GetMapping("/getGoodsById")
    public String getGoodsById(int id){
       return goodsService.getGoodsById(id);
    }
    @GetMapping(value = "/getGoodsByIdX",produces = "application/json;charset=utf-8")
    public String getGoodsByIdX(int id){
        return goodsService.getGoodsById(id);
    }

    //修改商品信息
    @PutMapping("/updateGoods")
    public String updateGoods(Goods goods){
        return goodsService.updateGoods(goods);
    }

    //随机查询10条数据
    @GetMapping(value = "/get10Goods",produces = "application/json;charset=utf-8")
    public String select10Goods(){

        return goodsService.select10Goods();
    }
}
