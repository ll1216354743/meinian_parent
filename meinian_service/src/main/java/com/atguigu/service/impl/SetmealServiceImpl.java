package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.SetmealService;
import com.atguigu.utils.QiniuUtils;
import com.atguigu.utils.RedisConstant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(Integer[] ids, Setmeal setmeal) {
        setmealDao.add(setmeal);
        if (ids != null) {
            addTsetmealTravelgroup(ids, setmeal.getId());
        }
        //将图片名称保存到Redis
        savePic2Redis(setmeal.getImg());
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //开启分页查询
        PageHelper.startPage(currentPage, pageSize);
        Page page = setmealDao.findPage(queryString);

        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public Setmeal getOneById(String id) {
        //通过id查询跟团旅信息
        Setmeal setmeal = setmealDao.getOneById(id);

        //通过跟团旅id查询出和自由行关联的id
        List<Integer> ids = setmealDao.getTravelGroupsById(id);
        //定义自由行List
        List<TravelGroup> travelGroups = new ArrayList<>();
        for (Integer oid : ids) {
            //声明对象
            TravelGroup travelGroup = new TravelGroup();
            //将每个与跟团旅关联的自由行id封装进Po
            travelGroup.setId(oid);
            //封装进List
            travelGroups.add(travelGroup);
        }


        //封装进travelGroup bean中的TravelItems属性中
        setmeal.setTravelGroups(travelGroups);

        return setmeal;
    }

    @Override
    public void delete(String id) {
        //通过id查询套餐数据
        Setmeal setmeal = setmealDao.getOneById(id);
        //将查询出的图片在七牛云中进行删除
        if (!"".equals(setmeal.getImg()) && setmeal.getImg() != null) {
            QiniuUtils.deleteFileFromQiniu(setmeal.getImg());
        }
        //删除套餐跟团旅中关联表数据
        setmealDao.deleteTsetmealTravelgroup(id);

        //删除redis中七牛云和数据库存放的数据
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, setmeal.getImg());
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());

        //删除套餐表中数据
        setmealDao.delete(id);
    }

    @Override
    public void update(Integer[] ids, Setmeal setmeal) {
        setmealDao.deleteTsetmealTravelgroup(setmeal.getId() + "");
        if (ids != null) {
            addTsetmealTravelgroup(ids, setmeal.getId());
        }
        setmealDao.update(setmeal);
    }

    @Override
    public List<Map> getSetmealReport() {
        List<Map> mapList = setmealDao.getSetmealReport();
        return mapList;
    }

    /**
     * 抽离添加套餐和跟团旅信息方法
     *
     * @param ids
     * @param setmealId
     */
    public void addTsetmealTravelgroup(Integer[] ids, Integer setmealId) {
        for (Integer id : ids) {
            Map map = new HashMap();
            map.put("setmeal_id", setmealId);
            map.put("travelgroup_id", id);
            setmealDao.addTsetmealTravelgroup(map);
        }

    }
}
