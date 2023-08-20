package com.atguigu.zhxy.service;

import com.atguigu.zhxy.pojo.Grade;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@Author: feifan
 *@Date: 2022/6/7 11:12
 */
public interface GradeService extends IService<Grade> {

    /**
     * 根据Name条件查询并分页
     * @param pageParam 具体分页
     * @param gradeName 查询条件
     * @return
     */
    IPage<Grade> getGradeByOpr(Page<Grade> pageParam,String gradeName);

    /**
     * 获取所有年级
     * @return
     */
    List<Grade> getGrades();
}
