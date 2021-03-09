package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    void add(Integer[] ids,Setmeal setmeal);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    Setmeal getOneById(String id);

    void delete(String id);

    void update(Integer[] ids, Setmeal setmeal);

    List<Map> getSetmealReport();

}
