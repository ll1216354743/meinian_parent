package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = TravelGroupService.class)
public class TravelGroupServiceImpl implements TravelGroupService {

    @Autowired
    private TravelGroupDao travelGroupDao;

    /**
     * 添加跟团旅信息
     * @param travelGroup
     * @param travelItemIds
     */
    @Override
    public void add(TravelGroup travelGroup, Integer[] travelItemIds) {
        //新增跟团游主表数据
        travelGroupDao.add(travelGroup);
        //如果选择了自由行
        if (travelItemIds != null){
            //循环插入跟团游和自由行中间表
            addOrUpdate(travelItemIds,travelGroup.getId());
        }
    }

    /**
     * 分页查询跟团旅
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //开启分页查询
        PageHelper.startPage(currentPage, pageSize);
        Page page = travelGroupDao.findPage(queryString);

        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    /**
     * 通过id删除跟团旅
     * @param id
     */
    @Override
    public void delete(String id) {
        travelGroupDao.deleteTravelgroupTravelitemById(id);
        travelGroupDao.delete(id);
    }

    /**
     * 通过id回显跟团旅和与之对应的自由行
     * @param id
     * @return
     */
    @Override
    public TravelGroup getOneById(String id) {

        //通过id查询跟团旅信息
        TravelGroup travelGroup = travelGroupDao.getOneById(id);
        //通过跟团旅id查询出和自由行关联的id
        List<Integer> ids = travelGroupDao.getTravelItemsById(id);
        //定义自由行List
        List<TravelItem> travelItems = new ArrayList<>();
        for (Integer oid : ids) {
            //声明对象
            TravelItem travelItem = new TravelItem();
            //将每个与跟团旅关联的自由行id封装进Po
            travelItem.setId(oid);
            //封装进List
            travelItems.add(travelItem);
        }
        //封装进travelGroup bean中的TravelItems属性中
        travelGroup.setTravelItems(travelItems);

        return travelGroup;
    }

    /**
     * 修改跟团旅信息
     * @param travelItemIds
     * @param travelGroup
     */
    @Override
    public void update(Integer[] travelItemIds, TravelGroup travelGroup) {
        //通过跟团旅的id删除关联表中所有关联的自由行id
        travelGroupDao.deleteTravelgroup_travelitemById(travelGroup.getId());
        //修改跟团旅主表数据
        travelGroupDao.update(travelGroup);
        if (travelItemIds != null){
            //循环插入跟团游和自由行中间表
            addOrUpdate(travelItemIds, travelGroup.getId());
        }
    }

    /**
     * 查询所有跟团旅
     * @return
     */
    @Override
    public List<TravelGroup> findAllGroup() {
        return travelGroupDao.findAllGroup();
    }

    /**
     * 插入自由行和跟团旅的中间表
     * @param travelItemIds
     * @param travelItemId
     */
    public void addOrUpdate(Integer[] travelItemIds,Integer travelItemId){
        for (Integer id : travelItemIds) {
            Map<String, Object> map = new HashMap<>();
            map.put("travelgroup_id", travelItemId);
            map.put("travelitem_id", id);
            travelGroupDao.addTravelgroup_travelitem(map);
        }
    }

}
