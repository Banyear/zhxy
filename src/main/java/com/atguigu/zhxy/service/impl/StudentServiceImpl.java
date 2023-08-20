package com.atguigu.zhxy.service.impl;

import com.atguigu.zhxy.mapper.StudentMapper;
import com.atguigu.zhxy.pojo.LoginForm;
import com.atguigu.zhxy.pojo.Student;
import com.atguigu.zhxy.service.StudentService;
import com.atguigu.zhxy.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *@Author: feifan
 *@Date: 2022/6/7 11:14
 */
@Api(tags = "学生控制器")
@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    /**
     * 学生登录
     * @param loginForm
     * @return
     */
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    /**
     * 根据id查找学生
     * @param userId
     * @return
     */
    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", userId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 带条件查询学生信息并分页
     * @param pageParam 分页信息
     * @param student 查询条件
     * @return
     */
    @Override
    public IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student) {
        QueryWrapper<Student> queryWrapper = null;
        if (student != null || "".equals(student)) {
            queryWrapper = new QueryWrapper<>();
            if (student.getClazzName() != null || "".equals(student.getClazzName())) {
                queryWrapper.eq("clazz_name", student.getClazzName());
            }
            if (student.getName() != null || "".equals(student.getName())) {
                queryWrapper.like("name", student.getName());
            }
            queryWrapper.orderByDesc("id");
            queryWrapper.orderByAsc("name");
        }
        // 创建分页对象
        IPage<Student> pages = baseMapper.selectPage(pageParam, queryWrapper);

        return pages;
    }


}
