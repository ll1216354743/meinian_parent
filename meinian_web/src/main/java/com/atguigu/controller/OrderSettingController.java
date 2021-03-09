package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import com.atguigu.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/OrderSetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;


    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            // 使用poi工具类解析excel文件，读取里面的内容
            List<String[]> lists = POIUtils.readExcel(excelFile);
            // 把List<String[]> 数据转换成 List<OrderSetting>数据
            List<OrderSetting> orderSettings = new ArrayList<>();
            //  迭代里面的每一行数据，进行封装到集合里面
            for (String[] str : lists) {
                // 获取到一行里面，每个表格数据，进行封装
                OrderSetting orderSetting = new OrderSetting(new Date(str[0]), Integer.parseInt(str[1]));
                orderSettings.add(orderSetting);
            }
            // 调用业务进行保存
            orderSettingService.add(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }


    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        try {
            List<Map> list = orderSettingService.getYearOrM(date);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.update(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
