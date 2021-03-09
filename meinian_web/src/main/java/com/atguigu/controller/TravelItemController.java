package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自由行模块
 */
@RestController
@RequestMapping("/TravelItem")
public class TravelItemController {

    @Reference //远程调用
    private TravelItemService travelItemService;

    /**
     * 添加自由行数据
     * @param travelItem
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TravelItem travelItem) {
        //System.out.println("新增方法");
        try {
            //如果新增方法没有报错,说明新增成功
            travelItemService.add(travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
    }

    /**
     * 分页查询自由行
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = travelItemService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        new Result(true, MessageConstant.QUERY_TRAVELITEM_FAIL);
        return pageResult;
    }

    /**
     * 通过id删除自由行
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE')")//权限校验，使用TRAVELITEM_DELETE123测试
    public Result delete(String id) {
        try {
            travelItemService.delete(id);
            return new Result(true, MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    /**
     * [初始化修改操作]获取自由行数据
     * @param id
     * @return
     */
    @RequestMapping("/getone")
    public Result getOne(String id) {

        TravelItem travelItem = travelItemService.getOneById(id);

        return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, travelItem);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TravelItem travelItem) {

        try {
            travelItemService.update(travelItem);
            return new Result(true, MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }



}