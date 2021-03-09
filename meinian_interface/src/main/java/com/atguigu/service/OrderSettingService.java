package com.atguigu.service;

import com.atguigu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> orderSettings);

    List<Map> getYearOrM(String date);

    void update(OrderSetting orderSetting);
}
