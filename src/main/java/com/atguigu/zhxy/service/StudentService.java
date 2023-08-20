package com.atguigu.zhxy.service;

import com.atguigu.zhxy.pojo.LoginForm;
import com.atguigu.zhxy.pojo.Student;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 *@Author: feifan
 *@Date: 2022/6/7 11:13
 */
public interface StudentService extends IService<Student> {
    /**
     * 学生登录
     * @param loginForm
     * @return
     */
    Student login(LoginForm loginForm);

    /**
     * 根据id查找学生
     * @param userId
     * @return
     */
    Student getStudentById(Long userId);

    /**
     * 带条件查询学生信息并分页
     * @param pageParam 分页信息
     * @param student 查询条件
     * @return
     */
    IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student);
}
