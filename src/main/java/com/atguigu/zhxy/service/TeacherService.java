package com.atguigu.zhxy.service;

import com.atguigu.zhxy.pojo.LoginForm;
import com.atguigu.zhxy.pojo.Teacher;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 *@Author: feifan
 *@Date: 2022/6/7 11:16
 */
public interface TeacherService extends IService<Teacher> {

    /**
     * 老师登录
     * @param loginForm
     * @return
     */
    Teacher login(LoginForm loginForm);

    /**
     * 根据id查找老师
     * @param userId
     * @return
     */
    Teacher getTeacherById(Long userId);

    /**
     * 带条件查询教师信息并分页
     * @param pageParam 分页信息
     * @param teacher 查询条件
     * @return
     */
    IPage<Teacher> getTeachersByOpr(Page<Teacher> pageParam, Teacher teacher);
}
