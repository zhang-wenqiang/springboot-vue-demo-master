package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.GoodsType;
import org.springframework.scheduling.support.SimpleTriggerContext;

/*
 *   时间：2022/5/30
 *
 *
 */
public interface GoodsTypeService extends IService<GoodsType> {

    //获取全部商品类型
    String getGoodsType();

    //分页查询

    String getPageType(String typeName,int pagenum,int pagesize);

    //删除类型
    String deleteType(int id);

    //修改类型
    String updateType(GoodsType goodsType);

    //添加商品
    String addType(GoodsType goodsType);
}
