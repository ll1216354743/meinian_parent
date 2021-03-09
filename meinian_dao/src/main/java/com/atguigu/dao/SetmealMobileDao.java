package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.TravelItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealMobileDao {
    List<Setmeal> findAll();

    List<TravelItem> getTravelGroupTravelItems(@Param("id") Integer id);
}
