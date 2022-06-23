package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Result;
import com.example.demo.entity.Goods;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/*
 *   时间：2022/5/26
 *
 *
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements GoodsService{

    @Autowired
    GoodsMapper goodsMapper;
    //分页查询
    @Override
    public String getGoodsList(String name, int pagenum, int pagesize) {
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        if (!name.equals("")){
            wrapper.like("goods_name",name);
        }
        Page<Goods> goodsPage = new Page<>(pagenum,pagesize);
        goodsMapper.selectPage(goodsPage, wrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("goods",goodsPage);
        map.put("total",goodsPage.getTotal());

        return JSON.toJSONString(map);
    }

    //根据id删除商品
    @Override
    public String deleteGoodsById(int id) {
        int i = goodsMapper.deleteById(id);
        if (i>0){
            return Result.success();
        }else {
            return Result.error();
        }
    }

    //添加商品
    @Override
    public String addGoods(Goods goods) {
        int insert = goodsMapper.insert(goods);
        if (insert>0){
            return Result.success();
        }
        return Result.error();
    }

    //根据id获取商品信息
    @Override
    public String getGoodsById(int id) {
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();

        wrapper.eq("id",id);
        Goods goods = goodsMapper.selectOne(wrapper);

        if (goods!=null){
            return Result.success(goods);
        }
        return Result.error();
    }

    //修改商品信息
    @Override
    public String updateGoods(Goods goods) {
        int i = goodsMapper.updateById(goods);
        if (i>0){
            return Result.success();
        }
        return Result.error();
    }

    //随机查询10条商品信息
    @Override
    public String select10Goods() {

        List<Goods> goods = goodsMapper.select10Goods();


        return JSON.toJSONString(goods);
    }
    //根据商品名字查询商品,一定要记住wrapper中的字段是数据库中的
    @Override
    public String selectGoodsByName(String goodsName) {
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        //System.out.println(goodsName);
        if (!goodsName.equals("")){
            wrapper.like("goods_name",goodsName);
        }

        List<Goods> goods = goodsMapper.selectList(wrapper);
        return JSON.toJSONString(goods);
    }
}
