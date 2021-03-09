package com.atguigu.dao;

import com.atguigu.pojo.User;
import org.apache.ibatis.annotations.Param;


public interface UserDao {

    User findUserByUsername(@Param("username") String username);

}
