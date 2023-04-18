package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserServiceImpl userService;

    //登录验证，接受前端的账号和密码
    @PostMapping( "/login")
    @CrossOrigin
    public String login(User user) {

        User res = userService.selectOne(user);
        if (res != null) {
            return Result.success(res);
        } else {
            return Result.error();
        }

    }
    @PostMapping(value = "/loginX", produces = "application/json;charset=utf-8")
    @CrossOrigin
    public String loginX(User user) {

        User res = userService.selectOne(user);
        if (res != null) {
            return Result.success(res);
        } else {
            return Result.error();
        }

    }

    //注册，接受一个用户对象
    @PostMapping( "/register")
    @CrossOrigin
    public String register(User user) {
        System.out.println(user);
        if (userMapper.selectByName(user.getName()) > 0) {
            return Result.error();
        }
        //设置为普通用户
        user.setRole(2);
        //System.out.println(user);
        int insert = userMapper.insert(user);

        if (insert == 1) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", user.getName());
            User res = userMapper.selectOne(queryWrapper);

            return Result.success(res);
        } else {
            return Result.error();
        }
    }
    @PostMapping(value = "/registerX", produces = "application/json;charset=utf-8")
    @CrossOrigin
    public String registerX(User user) {
        System.out.println(user);
        if (userMapper.selectByName(user.getName()) > 0) {
            return Result.error();
        }
        //设置为普通用户
        user.setRole(2);
        //System.out.println(user);
        int insert = userMapper.insert(user);

        if (insert == 1) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", user.getName());
            User res = userMapper.selectOne(queryWrapper);

            return Result.success(res);
        } else {
            return Result.error();
        }
    }

    //查询除了当前用户外所有用户
    @GetMapping("/getUserList")
    @CrossOrigin
    public String selectAllUsers(String name, int id) {


        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!name.equals("")) {
            queryWrapper.like("name", name);
        }
        queryWrapper.ne("id", id);
        List<User> users = userMapper.selectList(queryWrapper);

        HashMap<String, Object> map = new HashMap<>();
        map.put("total", users.size());
        map.put("users", users);

        return JSON.toJSONString(map);

    }

    //分页查询，查询除了当前用户的所有用户
    @GetMapping("/pageSelect")
    @CrossOrigin
    public String pageSelect(String name, int id, int pagenum, int pagesize) {

        return userService.pageSelect(name, id, pagenum, pagesize);
    }

    //根据id查询用户
    @GetMapping("/getUserById")
    @CrossOrigin
    public String getUserById(int id) {
        //根据id查询用户
        User user = userMapper.selectById(id);
        //判断是否查询到用户，根据情况返回
        if (user != null) {
            return Result.success(user);
        }

        return Result.error();

    }

    //修改用户信息
    @PutMapping("/updateUserInfo")
    @CrossOrigin
    public String updateUserInfo(User user) {
        int i = userMapper.updateById(user);

        if (i > 0) {
            return Result.success();
        }
        return Result.error();
    }


    //根据id删除用户
    @DeleteMapping("/deleteUserById")
    @CrossOrigin
    public String deleteUserById(int id) {
        int i = userService.deleteUserById(id);

        if (i > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    //根据id分配用户角色
    @PutMapping("/updateRoleById")
    @CrossOrigin
    public String updateRoleById(int id, int role) {

        int i = userService.updateRoleByUserId(id, role);
        if (i > 0) {
            return Result.success();
        }
        return Result.error();
    }


}
