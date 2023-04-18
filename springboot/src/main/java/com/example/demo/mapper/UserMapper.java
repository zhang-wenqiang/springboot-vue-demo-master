package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Map;


@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 查询用户名
    @Select("select count(name) from user where account=#{name}")
    int selectByName(String name);

    @Update("update user set password = #{newPass} where id = #{userId}")
    int updatePass(Map<String, Object> map);


    int insertUser(@Param("user") User user);

    @Update("update user set role = #{role} where id = #{id}")
    int updateRole(int id,int role);



}
