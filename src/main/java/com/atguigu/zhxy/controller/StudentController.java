package com.atguigu.zhxy.controller;

import com.atguigu.zhxy.pojo.Student;
import com.atguigu.zhxy.service.StudentService;
import com.atguigu.zhxy.util.MD5;
import com.atguigu.zhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *@Author: feifan
 *@Date: 2022/6/7 21:11
 */
@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @ApiOperation("删除一个或者多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @ApiParam("多个学生id的JSON") @RequestBody List<Integer> ids
    ) {
        studentService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("增加和修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("JSON格式的Student对象") @RequestBody Student student) {
        Integer id = student.getId();
        // 对学生的密码进行加密
        if (id == null || 0 == id) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        // 保存学生信息进入数据库
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @ApiOperation("带条件分页查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentsByOpr(
            @ApiParam("页码数") @PathVariable Integer pageNo,
            @ApiParam("页大小") @PathVariable Integer pageSize,
            @ApiParam("查询条件") Student student
    ) {
        // 准备分页信息封装的page对象
        Page<Student> page = new Page<>(pageNo, pageSize);
        // 获取分页的学生信息
        IPage<Student> iPage = studentService.getStudentByOpr(page, student);
        return Result.ok(iPage);
    }
}
