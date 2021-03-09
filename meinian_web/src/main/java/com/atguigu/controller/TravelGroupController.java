package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelGroupService;
import com.atguigu.service.TravelItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/TravelGroup")
public class TravelGroupController {

    @Reference
    private TravelItemService travelItemService;
    @Reference
    private TravelGroupService travelGroupService;

    /**
     * 查询所有自由行数据
     *
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        List<TravelItem> lists = travelItemService.findAll();
        Result result = new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, lists);
        return result;
    }

    /**
     * 查询所有跟团旅
     * @return
     */
    @RequestMapping("/findAllGroup")
    public Result findAllGroup() {
        List<TravelGroup> lists = travelGroupService.findAllGroup();
        Result result = new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, lists);
        return result;
    }


    /**
     * 新增跟团游数据
     *
     * @param travelItemIds
     * @param travelGroup
     * @return
     */
    @RequestMapping("/add")
    public Result add(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup) {
        try {
            travelGroupService.add(travelGroup, travelItemIds);
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = travelGroupService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }


    @RequestMapping("/delete")
    public Result delete(String id) {
        try {
            travelGroupService.delete(id);
            return new Result(true, MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }


    @RequestMapping("/getOne")
    public Result getOne(String id) {
        try {
            TravelGroup travelGroup = travelGroupService.getOneById(id);
            return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, travelGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }


    @RequestMapping("/update")
    public Result update(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup) {
        try {
            travelGroupService.update(travelItemIds, travelGroup);
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_FAIL);
        }
    }


}
