package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Order;
import com.atguigu.service.OrderService;
import com.atguigu.utils.SMSUtils;
import com.atguigu.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/Order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 发送短信
     *
     * @param telephone
     * @return
     */
    @RequestMapping("/sendTheVerificationCode")
    public Result sendTheVerificationCode(String telephone) {
        //随机生成6位数字验证码
        int code = ValidateCodeUtils.generateValidateCode(6);
        try {
            //发送验证码
            SMSUtils.sendShortMessage(telephone, code + "");
            //向redis中存入当前手机号数据
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code + "");
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS, code);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 提交预约信息
     * @param map
     * @return
     */
    @RequestMapping("/save")
    public Result save(@RequestBody Map map) {
        try {
            //获取手机号
            String telephone = (String)map.get("telephone");
            //获取页面输入验证码
            String validateCode = (String)map.get("validateCode");
            //获取redis中该手机号存放的验证码
            String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            //redis中没有该手机验证码
            if (redisCode == null) {
                return new Result(false, MessageConstant.VALIDATECODE_NUTNULLERROR);
            } else {
                //验证码不匹配
                if (!redisCode.equals(validateCode)) {
                    return new Result(false, MessageConstant.VALIDATECODE_ERROR);
                } else {
                   //验证码通过
                    return orderService.save(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.ORDER_FAIL);
        }
    }

    /**
     * 查询预约信息
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {

        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
