package com.atguigu.dao;

import com.atguigu.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    Order findOrderByMap(Map<String, Object> param);

    void saveOrder(Order order);

    Map findById(@Param("id") Integer id);

    int getTodayOrderNumber(String date);
    int getTodayVisitsNumber(String date);
    int getThisWeekAndMonthOrderNumber(Map<String, Object> map);
    int getThisWeekAndMonthVisitsNumber(Map<String, Object> map);
    List<Map<String,Object>> findHotSetmeal();
}
