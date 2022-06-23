package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Goods;
import com.example.demo.entity.GoodsType;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.mapper.GoodsTypeMapper;
import com.example.demo.service.impl.GoodsTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 *   时间：2022/5/30
 *
 *
 */
@RestController
@RequestMapping("/goodsType")
@CrossOrigin
public class GoodsTypeController {

    @Autowired
    GoodsTypeMapper goodsTypeMapper;


    @Autowired
    GoodsMapper goodsMapper;


    @Autowired
    GoodsTypeServiceImpl goodsTypeService;


    //获取全部商品类型
    @GetMapping("/getGoodsType")
    public String getType() {
        return goodsTypeService.getGoodsType();
    }


    //分页查询
    @GetMapping("/getPageType")
    public String getTy(String type, int pagenum, int pagesize) {
        return goodsTypeService.getPageType(type, pagenum, pagesize);
    }

    //删除商品类型
    @DeleteMapping("/deleteType")
    public String getType(int id) {

        return goodsTypeService.deleteType(id);
    }

    //修改类型
    @PutMapping("updateType")
    public String updateType(GoodsType goodsType) {
        return goodsTypeService.updateType(goodsType);
    }

    //添加商品类型
    @PostMapping("/addGoodsType")
    public String addGoodsType(GoodsType goodsType) {
        System.out.println(goodsType);
        return goodsTypeService.addType(goodsType);
    }
}
