package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public Map getMemberReport() {

        Map map = new HashMap();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);

        List<String> months = new ArrayList();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }

        List<Integer> memberCount = new ArrayList<>();
        for (String month : months) {
            int count = memberDao.getMemberCountByMonth(month);
            memberCount.add(count);
        }

        map.put("memberCount",memberCount);
        map.put("months",months);

        return map;
    }

}
