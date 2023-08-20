package com.atguigu.zhxy.service.impl;

import com.atguigu.zhxy.mapper.TeacherMapper;
import com.atguigu.zhxy.pojo.LoginForm;
import com.atguigu.zhxy.pojo.Student;
import com.atguigu.zhxy.pojo.Teacher;
import com.atguigu.zhxy.service.TeacherService;
import com.atguigu.zhxy.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 *@Author: feifan
 *@Date: 2022/6/7 11:17
 */
@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    /**
     * 老师登录
     * @param loginForm
     * @return
     */
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    /**
     * 根据id查找老师
     * @param userId
     * @return
     */
    @Override
    public Teacher getTeacherById(Long userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", userId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 带条件查询教师信息并分页
     * @param pageParam 分页信息
     * @param teacher 查询条件
     * @return
     */
    @Override
    public IPage<Teacher> getTeachersByOpr(Page<Teacher> pageParam, Teacher teacher) {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        if (teacher != null || "".equals(teacher)) {
            //班级名称条件
            if (!StringUtils.isEmpty(teacher.getClazzName())) {
                queryWrapper.eq("clazz_name",teacher.getClazzName());
            }
            //教师名称条件
            if (!StringUtils.isEmpty(teacher.getName())) {
                queryWrapper.like("name",teacher.getName());
            }
            queryWrapper.orderByDesc("id");
            queryWrapper.orderByAsc("name");
        }
        IPage<Teacher> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }
}