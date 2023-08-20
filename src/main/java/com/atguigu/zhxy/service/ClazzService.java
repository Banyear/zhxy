package com.atguigu.zhxy.service;

import com.atguigu.zhxy.pojo.Clazz;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@Author: feifan
 *@Date: 2022/6/7 10:48
 */
public interface ClazzService extends IService<Clazz> {

    /**
     * 带条件查询班级信息并分页
     * @param pageParam
     * @param clazz
     * @return
     */
    IPage<Clazz> getclazzByOpr(Page<Clazz> pageParam, Clazz clazz);

    /**
     * 查询所有班级信息
     * @return
     */
    List<Clazz> getClazzs();
}
