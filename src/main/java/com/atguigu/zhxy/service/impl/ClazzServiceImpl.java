package com.atguigu.zhxy.service.impl;

import com.atguigu.zhxy.mapper.ClazzMapper;
import com.atguigu.zhxy.pojo.Clazz;
import com.atguigu.zhxy.service.ClazzService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 *@Author: feifan
 *@Date: 2022/6/7 10:49
 */
@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public IPage<Clazz> getclazzByOpr(Page<Clazz> pageParam, Clazz clazz) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (clazz != null || "".equals(clazz)) {
            String gradeName = clazz.getGradeName();
            // 年级名称条件
            if (!StringUtils.isEmpty(gradeName)) {
                queryWrapper.eq("grade_name", gradeName);
            }
            //班级名称条件
            String clazzName = clazz.getName();
            if (!StringUtils.isEmpty(clazzName)) {
                queryWrapper.like("name", clazzName);
            }
            queryWrapper.orderByDesc("id");
            queryWrapper.orderByAsc("name");
        }
        return baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public List<Clazz> getClazzs() {
        return baseMapper.selectList(null);
    }
}
