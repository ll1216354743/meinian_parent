package com.atguigu.dao;

import org.apache.ibatis.annotations.Param;

import com.atguigu.pojo.Member;

public interface MemberDao {

    Member findTelephone(@Param("phoneNumber") String telephone);

    void add(Member member);

    int getMemberCountByMonth(@Param("month") String month);

    public Integer findMemberCountBeforeDate(String date);
    int getTodayNewMember(String date);
    int getTotalMember();
    int getThisWeekAndMonthNewMember(String date);
}
