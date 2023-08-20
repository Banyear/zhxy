package com.atguigu.zhxy.controller;

import com.atguigu.zhxy.pojo.Grade;
import com.atguigu.zhxy.service.GradeService;
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
@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("获取全部年级")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }

    @ApiOperation("删除一个或者多个grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("JSON的年级id集合,映射为后台List<Integer>") @RequestBody List<Integer> ids
    ) {
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("添加或者修改年级信息")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("JSON的grade对象转换后台数据模型") @RequestBody Grade grade
    ) {
        // 调用服务层,添加或修改年纪信息
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("查询年级信息,分页带条件")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询模糊匹配班级名") String gradeName

    ) {
        // 分页 带条件查询
        Page<Grade> page = new Page<>(pageNo, pageSize);
        // 通过服务层,传入分页信息,和查询的条件
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page, gradeName);
        // 封装Result对象并返回
        return Result.ok(pageRs);
    }
}
