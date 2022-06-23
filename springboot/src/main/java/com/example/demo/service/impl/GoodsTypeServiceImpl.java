package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Result;
import com.example.demo.entity.Goods;
import com.example.demo.entity.GoodsType;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.mapper.GoodsTypeMapper;
import com.example.demo.service.GoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/*
 *   时间：2022/5/30
 *
 *
 */
@Service
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {
    @Autowired
    GoodsTypeMapper goodsTypeMapper;

    @Autowired
    GoodsMapper goodsMapper;

    //获取全部商品类型
    @Override
    public String getGoodsType() {
        List<GoodsType> goodsTypes = goodsTypeMapper.selectList(null);

        return JSON.toJSONString(goodsTypes);
    }

    //分页查询
    @Override
    public String getPageType(String typeName, int pagenum, int pagesize) {
        QueryWrapper<GoodsType> wrapper = new QueryWrapper<>();
        if (!typeName.equals("")){
            wrapper.like("type_name",typeName);
        }
        Page<GoodsType> page = new Page<>(pagenum,pagesize);

        Page<GoodsType> goodsTypePage = goodsTypeMapper.selectPage(page, wrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", goodsTypePage.getTotal());
        map.put("types", goodsTypePage);
        return JSON.toJSONString(map);
    }

    //删除类型
    @Override
    public String deleteType(int id) {
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_type",id);

        if (goodsMapper.selectCount(wrapper)>0){
            return Result.errMsg("有商品依赖，无法删除");
        }


        int i = goodsTypeMapper.deleteById(id);
        if (i>0){
            return Result.successMsg("删除成功");
        }
        return Result.errMsg("删除失败");
    }

    //修改类型
    @Override
    public String updateType(GoodsType goodsType) {
        int i = goodsTypeMapper.updateById(goodsType);

        if (i>0){
            return Result.success();
        }
        return  Result.error();
    }

    //添加商品

    @Override
    public String addType(GoodsType goodsType) {


        int insert = goodsTypeMapper.insert(goodsType);
        if (insert>0){
            return Result.successMsg("添加成功");
        }
        return Result.errMsg("添加失败");
    }
}
