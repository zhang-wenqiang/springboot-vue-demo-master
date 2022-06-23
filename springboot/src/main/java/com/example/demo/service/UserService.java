package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

/*
 *   时间：2022/5/26
 *
 *
 */
public interface UserService extends IService<User> {


    int selectByName(String name);


    int updatePass(Map<String, Object> map);


    int insertUser(User user);

    //登录验证

    User selectOne(User user);

    int deleteUserById(int id);

    //根据用户角色修改role
    int updateRoleByUserId(int id,int role);


    //分页查询
    String pageSelect(String name, int id,int pagenum,int pagesize);


}
