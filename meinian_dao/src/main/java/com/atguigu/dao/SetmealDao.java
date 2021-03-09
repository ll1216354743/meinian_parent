package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {


    void add(Setmeal setmeal);

    void addTsetmealTravelgroup(Map map);

    Page findPage(@Param("queryString") String queryString);

    Setmeal getOneById(String id);

    List<Integer> getTravelGroupsById(String id);

    void deleteTsetmealTravelgroup(@Param("id") String id);

    void delete(@Param("id") String id);

    void update(Setmeal setmeal);

    List<TravelGroup> getTravelGroups(@Param("id") String id);

    List<Map> getSetmealReport();
}
