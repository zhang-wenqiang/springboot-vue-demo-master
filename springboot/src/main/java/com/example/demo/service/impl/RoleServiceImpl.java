package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Result;
import com.example.demo.entity.Goods;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *   时间：2022/5/31
 *
 *
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {



    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserMapper userMapper;

    //分页查询
    @Override
    public String getRole(String roleName, int pagenum, int pagesize) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();

        if (!roleName.equals("")){
            wrapper.like("role_name",roleName);
        }
        Page<Role> rolePage = new Page<>(pagenum,pagesize);

        Page<Role> rolePage1 = roleMapper.selectPage(rolePage, wrapper);
        Map<String, Object> map = new HashMap<>();

        map.put("total",rolePage1.getTotal());
        map.put("roles",rolePage1);

        return JSON.toJSONString(map);
    }



    //查询全部角色
    @Override
    public String getAllRole() {
        List<Role> roles = roleMapper.selectList(null);


        return JSON.toJSONString(roles);
    }

    //修改角色
    @Override
    public String updateRole(Role role) {
        int i = roleMapper.updateById(role);
        if (i>0){
            return Result.successMsg("修改成功");
        }else {
            return Result.errMsg("无需修改");
        }
    }

    //添加角色
    @Override
    public String addRole(Role role) {
        int insert = roleMapper.insert(role);
        if (insert>0){
            return Result.successMsg("添加成功");
        }
        return Result.errMsg("无需添加");
    }

    //删除角色
    @Override
    public String deleteRole(int id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("role",id);

        if (userMapper.selectCount(wrapper)>0){
            return Result.errMsg("有用户依赖，无法删除");
        }

        int i = roleMapper.deleteById(id);
        if (i>0){
            return Result.successMsg("删除成功");
        }
        return Result.errMsg("删除失败");
    }
}
