package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/*
 *   时间：2022/5/25
 *
 *
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

//随机查询10条商品信息

    @Select("SELECT\n" +
            "    *\n" +
            "FROM\n" +
            "    `goods`\n" +
            "WHERE\n" +
            "    id >= (\n" +
            "        SELECT\n" +
            "            floor(\n" +
            "                RAND() * (SELECT MAX(id) FROM `goods`)\n" +
            "            )\n" +
            "    )\n" +
            "ORDER BY\n" +
            "    id\n" +
            "LIMIT 10;")
    List<Goods> select10Goods();

    //商品数量减1
    @Update("update goods set amount=amount-1 where id = #{id}")
    int updateGoodsAmount(int id);

}
