package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {

    void add(OrderSetting orderSetting);

    int findOrderSettingByOrderDate(@Param("date") Date orderDate);

    void update(OrderSetting orderSetting);

    List<OrderSetting> getYearOrM(@Param("date") String date);

    OrderSetting findByOrderDate(@Param("date") Date date);

    void editOrderDateByDate(@Param("date") Date date);
}
