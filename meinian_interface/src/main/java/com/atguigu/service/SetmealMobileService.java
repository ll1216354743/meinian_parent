package com.atguigu.service;

import com.atguigu.pojo.Setmeal;

import java.util.List;

public interface SetmealMobileService {
    List<Setmeal> findAll();

    Setmeal getOneById(String id);
}
