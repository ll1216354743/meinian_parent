package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;

import java.util.List;

public interface TravelGroupService {
    void add(TravelGroup travelGroup, Integer[] travelItemIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void delete(String id);

    TravelGroup getOneById(String id);

    void update(Integer[] travelItemIds, TravelGroup travelGroup);

    List<TravelGroup> findAllGroup();
}
