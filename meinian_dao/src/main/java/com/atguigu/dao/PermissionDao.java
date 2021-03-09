package com.atguigu.dao;

import com.atguigu.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.Set;


public interface PermissionDao {

    Set<Permission> findPermissionsByRoleId(@Param("id") Integer id);

}
