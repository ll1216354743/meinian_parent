package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.SetmealDao;
import com.atguigu.dao.SetmealMobileDao;
import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.SetmealMobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(interfaceClass = SetmealMobileService.class)
public class SetmealMobileServiceImpl implements SetmealMobileService {

    @Autowired
    private SetmealMobileDao setmealMobileDao;

    @Autowired
    private SetmealDao setmealDao;


    @Override
    public List<Setmeal> findAll() {
        return setmealMobileDao.findAll();
    }

    @Override
    public Setmeal getOneById(String id) {
        //通过id查询套餐信息
        Setmeal setmeal = setmealDao.getOneById(id);
        //通过套餐ID查询所有跟团旅
        List<TravelGroup> travelGroups = setmealDao.getTravelGroups(id);
        for (TravelGroup travelGroup : travelGroups) {
            //通过跟团旅ID查询所有自由行
            List<TravelItem> travelItems = setmealMobileDao.getTravelGroupTravelItems(travelGroup.getId());
            travelGroup.setTravelItems(travelItems);
        }
        //封装进travelGroup bean中的TravelGroups属性中
        setmeal.setTravelGroups(travelGroups);

        return setmeal;
    }

}
