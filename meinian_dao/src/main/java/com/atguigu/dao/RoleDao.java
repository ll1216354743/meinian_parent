package com.atguigu.dao;

import com.atguigu.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface RoleDao {

    Set<Role> findRolesByUserId(@Param("id") Integer id);

}
