package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Role;

/*
 *   时间：2022/5/31
 *
 *
 */
public interface RoleService extends IService<Role> {

    //分页查询


    String getRole(String roleName,int pagenum,int pagesize);

    //查询全部角色

    String getAllRole();

    //修改角色
    String updateRole(Role role);


    //添加角色
    String addRole(Role role);

    //删除角色
    String deleteRole(int id);
}
