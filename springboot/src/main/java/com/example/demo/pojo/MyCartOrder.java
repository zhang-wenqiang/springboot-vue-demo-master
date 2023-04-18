package com.example.demo.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
 *   时间：2022/6/9
 *
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCartOrder {
    private Integer orderid;

    private Integer id;
    private String goodsName;
    private int amount;
    private int price;
    private String img;
    private int goodsType;

    private Date createTime;

    private Date updateTime;
}
