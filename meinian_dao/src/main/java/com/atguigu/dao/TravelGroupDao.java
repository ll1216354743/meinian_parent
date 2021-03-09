package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {

    void add(TravelGroup travelGroup);

    void addTravelgroup_travelitem(Map<String, Object> map);

    Page findPage(@Param("queryString") String queryString);

    void deleteTravelgroupTravelitemById(String id);

    void delete(@Param("id") String id);

    TravelGroup getOneById(@Param("id") String id);

    List<Integer> getTravelItemsById(String id);

    void deleteTravelgroup_travelitemById(@Param("id") Integer id);

    void update(TravelGroup travelGroup);

    List<TravelGroup> findAllGroup();
}
