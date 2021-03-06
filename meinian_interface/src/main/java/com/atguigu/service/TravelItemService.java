package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.pojo.TravelItem;

import java.util.List;

public interface TravelItemService {

    void add(TravelItem travelItem);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void delete(String id);

    TravelItem getOneById(String id);

    void update(TravelItem travelItem);

    List<TravelItem> findAll();

}
