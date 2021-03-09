package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import com.atguigu.utils.SMSUtils;
import com.atguigu.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/Login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 登录获取验证码
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
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60, code + "");
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS, code);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 登录操作
     * @param map
     * @param response
     * @return
     */
    @RequestMapping("/check")
    public Result check(@RequestBody Map map, HttpServletResponse response) {
        try {
            //获取手机号
            String telephone = (String)map.get("telephone");
            //获取页面输入验证码
            String validateCode = (String)map.get("validateCode");
            //获取redis中该手机号存放的验证码
            String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            //redis中没有该手机验证码
            if (redisCode == null) {
                return new Result(false, MessageConstant.VALIDATECODE_NUTNULLERROR);
            } else {
                //验证码不匹配
                if (!redisCode.equals(validateCode)) {
                    return new Result(false, MessageConstant.VALIDATECODE_ERROR);
                } else {
                    //验证码通过
                    Member member = memberService.findByTelephone(telephone);
                    if (member == null) {
                        member = new Member();
                        member.setPhoneNumber(telephone);
                        member.setRegTime(new Date());

                        memberService.add(member);
                    }
                    //登录成功 写入Cookie，跟踪用户
                    Cookie cookie = new Cookie("login_member_telephone",telephone);
                    cookie.setPath("/");//路径
                    cookie.setMaxAge(60*60*24*30);//有效期30天（单位秒）
                    response.addCookie(cookie);
                    return new Result(true, MessageConstant.LOGIN_SUCCESS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.ORDER_FAIL);
        }
    }


}
