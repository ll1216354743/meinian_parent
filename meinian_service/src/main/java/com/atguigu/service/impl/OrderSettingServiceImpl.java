package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 导入预约数据
     * @param orderSettings
     */
    @Override
    public void add(List<OrderSetting> orderSettings) {
        for (OrderSetting orderSetting : orderSettings) {
            int count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());
            if (count == 1) {
                orderSettingDao.update(orderSetting);
            } else {
                orderSettingDao.add(orderSetting);
            }
        }
    }

    /**
     * 通过年月查询预约数据
     * @param date 日期格式:yyyy-MM
     * @return
     */
    @Override
    public List<Map> getYearOrM(String date) {

        List<OrderSetting> orderSettings = orderSettingDao.getYearOrM(date);

        List<Map> mapList = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettings) {
            Map map = new HashMap();
            map.put("date", orderSetting.getOrderDate().getDate());
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public void update(OrderSetting orderSetting) {
        int count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            orderSettingDao.update(orderSetting);
        } else {
            orderSettingDao.add(orderSetting);
        }

    }
}
