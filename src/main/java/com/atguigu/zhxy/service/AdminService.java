package com.atguigu.zhxy.service;

import com.atguigu.zhxy.pojo.Admin;
import com.atguigu.zhxy.pojo.LoginForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 *@Author: feifan
 *@Date: 2022/6/7 10:44
 */
public interface AdminService extends IService<Admin> {
    /**
     * 登录
     * @param loginForm
     * @return
     */
    Admin login(LoginForm loginForm);

    /**
     * 根据id查询admin
     * @param userId
     */
    Admin getAdminById(Long userId);

    /**
     * 根据管理员姓名查询管理员并分页
     * @param pageParam 分页信息
     * @param adminName 管理员姓名
     * @return
     */
    IPage<Admin> getAdmins(Page<Admin> pageParam, String adminName);
}
