package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/*
 *   时间：2022/5/26
 *
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    OrderMapper orderMapper;


    @Override
    public int selectByName(String name) {
        return userMapper.selectByName(name);
    }

    @Override
    public int updatePass(Map<String, Object> map) {
        return userMapper.updatePass(map);
    }

    @Override

    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    //登录验证
    @Override
    public User selectOne(User user) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", user.getAccount())
                .eq("password", user.getPassword());
        User res = userMapper.selectOne(queryWrapper);
        return res;
    }

    //根据id删除用户
    @Override
    public int deleteUserById(int id) {
        //先删除用户所有订单

        orderMapper.delete(id);
        int i = userMapper.deleteById(id);
        return i;
    }
    //根据用户角色修改role
    @Override
    public int updateRoleByUserId(int id, int role) {

        int i = userMapper.updateRole(id, role);
        return i;
    }

    //分页查询
    @Override
    public String pageSelect(String name, int id, int pagenum, int pagesize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!name.equals("")) {
            queryWrapper.like("name", name);
        }
        queryWrapper.ne("id", id);
        Page<User> userPage = new Page<>(pagenum,pagesize);
        Page<User> users = userMapper.selectPage(userPage, queryWrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", users.getTotal());
        map.put("users", users);

        return JSON.toJSONString(map);
    }
}
