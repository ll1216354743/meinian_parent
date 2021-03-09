package com.atguigu.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional //事务注解
@Service(interfaceClass = TravelItemService.class)
public class TravelItemServiceImpl implements TravelItemService {

    @Autowired
    private TravelItemDao travelItemDao;

    /**
     * 自由行添加操作
     * @param travelItem
     */
    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        //开启分页功能
        //param1:当前第几页
        //param2:一页多少数据
        PageHelper.startPage(currentPage, pageSize);

        //查询条件sql  queryString:携带的条件查询
        Page page = travelItemDao.findPage(queryString);

        //返回查询条件
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());

        return pageResult;
    }

    /**
     * 删除自由行
     * @param id
     */
    @Override
    public void delete(String id) {
        travelItemDao.delete(id);
    }

    /**
     * 通过ID获取自由行
     * @param id
     * @return
     */
    @Override
    public TravelItem getOneById(String id) {
        return travelItemDao.getOneById(id);
    }

    /**
     * 编辑自由行
     * @param travelItem
     */
    @Override
    public void update(TravelItem travelItem) {
        travelItemDao.update(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }

}
