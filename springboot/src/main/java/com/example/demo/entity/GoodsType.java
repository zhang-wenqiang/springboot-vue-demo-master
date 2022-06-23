package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *   时间：2022/5/30
 *
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsType {


    @TableId(type = IdType.AUTO)
    private Integer id;

    private String typeName;
}
