package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import com.atguigu.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.pojo.Member;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Transactional
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;


    /**
     * 保存预约数据
     * @param map
     */
    @Override
    public Result save(Map map) throws Exception {

        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        //获取前台选择的预约日期
        String orderDate = (String) map.get("orderDate");
        //转换为日期对象
        Date date = DateUtils.parseString2Date(orderDate);
        //查询该日期中在后台有没有进行预约设置
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);

        //后台没有做预约设置,我们不能进行预约
        if (orderSetting == null) {
            return new Result(false, MessageConstant.ORDERSETTING_IS_NULL);
        } else {
            //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
            //已预约人数
            int reservations = orderSetting.getReservations();
            //设置最大预约人数
            int number = orderSetting.getNumber();
            if (reservations >= number) {
                return new Result(false, MessageConstant.ORDER_FULL);
            }
        }

        //3、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findTelephone(telephone);
        Order order = null;
        if (member == null) {//如果不是会员
            //注册会员
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setIdCard((String) map.get("idCard"));
            member.setPhoneNumber((String) map.get("telephone"));
            member.setRegTime(date);
            memberDao.add(member);
        }

        //4、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        Map<String, Object> param = new HashMap<>();
        param.put("memberId", member.getId());
        param.put("orderDate", date);
        String setmealId = (String) map.get("setmealId");
        param.put("setmealId", Integer.parseInt(setmealId));

        order = orderDao.findOrderByMap(param);

        if (order != null) {
            //所选日期已经约过,不能重复进行预约
            return new Result(false, MessageConstant.HAS_ORDERED);
        }else{
            order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(date);
            order.setOrderType(Order.ORDERTYPE_WEIXIN);
            order.setOrderStatus(Order.ORDERSTATUS_NO);
            order.setSetmealId(Integer.parseInt(setmealId));

            orderDao.saveOrder(order);
        }

        //5、预约成功，更新当日的已预约人数
        orderSettingDao.editOrderDateByDate(date);

        //返回成功数据
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById(id);
        Date orderDate = (Date)map.get("orderDate");
        map.put("orderDate", DateUtils.parseDate2String(orderDate));
        return map;
    }
}
