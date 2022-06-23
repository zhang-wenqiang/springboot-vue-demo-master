package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Role;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.util.HashMap;
import java.util.Map;

/*
 *   时间：2022/5/31
 *
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleMapper roleMapper;


    @Autowired
    RoleServiceImpl roleService;


    //分页查询角色信息

    @GetMapping("/getRole")
    public String getRole(String roleName,int pagenum,int pagesize){
        return roleService.getRole(roleName, pagenum,pagesize);
    }

    //查询全部角色信息

    @GetMapping("/getAllRole")
    public String getAllRole(){
        return roleService.getAllRole();
    }

    //修改角色
    @PutMapping("/updateRole")
    public String updateRole(Role role){

        System.out.println(role);
        return roleService.updateRole(role);
    }

    //添加角色
    @PostMapping("/addRole")
    public String addRole(Role role){


        return roleService.addRole(role);
    }

    //删除角色
    @DeleteMapping("deleteRole")
    public String deleteRole(int id){
        return roleService.deleteRole(id);
    }

}
