package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.utils.QiniuUtils;
import com.atguigu.utils.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/Setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 上传文件
     *
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            // 找到.最后出现的位置
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);

            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);

            //图片上传成功
            Result result = new Result(true, MessageConstant.UPLOAD_SUCCESS, fileName);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }


    @RequestMapping("/add")
    public Result add(Integer[] ids, @RequestBody Setmeal setmeal) {
        try {
            setmealService.add(ids, setmeal);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }


    @RequestMapping("/getOne")
    public Result getOne(String id) {
        try {
            Setmeal setmeal = setmealService.getOneById(id);
            return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/delete")
    public Result delete(String id) {
        try {
            setmealService.delete(id);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/update")
    public Result update(Integer[] ids, @RequestBody Setmeal setmeal) {
        try {
            setmealService.update(ids, setmeal);
            return new Result(true, "编辑套餐成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "编辑套餐失败");
        }
    }
}
