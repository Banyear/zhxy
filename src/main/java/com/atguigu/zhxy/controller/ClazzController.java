package com.atguigu.zhxy.controller;

import com.atguigu.zhxy.pojo.Clazz;
import com.atguigu.zhxy.service.ClazzService;
import com.atguigu.zhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *@Author: feifan
 *@Date: 2022/6/7 21:11
 */
@Api(tags = "班级管理器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @ApiOperation("获取所有班级的JSON")
    @GetMapping("/getClazzs")
    public Result getClazzs() {
        List<Clazz> clazzList = clazzService.getClazzs();
        return Result.ok(clazzList);
    }

    @ApiOperation("删除一个或者多个clazz信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
            @ApiParam("JSON的班级id集合,映射为后台List<Integer>") @RequestBody List<Integer> ids
    ) {
        clazzService.removeByIds(ids);
        return Result.ok();
    }


    @ApiOperation("增加或者修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("JSON格式的班级信息") @RequestBody Clazz clazz
    ) {
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("分页待条件查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的查询条件") Clazz clazz
    ) {
        // 准备分页信息封装的page对象
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        // 获取分页的班级信息
        IPage<Clazz> iPage = clazzService.getclazzByOpr(page, clazz);

        return Result.ok(iPage);
    }
}
