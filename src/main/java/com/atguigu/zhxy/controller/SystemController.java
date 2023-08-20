package com.atguigu.zhxy.controller;

import com.atguigu.zhxy.pojo.Admin;
import com.atguigu.zhxy.pojo.LoginForm;
import com.atguigu.zhxy.pojo.Student;
import com.atguigu.zhxy.pojo.Teacher;
import com.atguigu.zhxy.service.AdminService;
import com.atguigu.zhxy.service.StudentService;
import com.atguigu.zhxy.service.TeacherService;
import com.atguigu.zhxy.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *@Author: feifan
 *@Date: 2022/6/7 21:16
 */
@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("更新用户密码的处理器")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            // token过期
            return Result.fail().message("token失效,请重新登录后修改密码");
        }
        // 获取用户ID和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);

        switch (userType) {
            case 1:
                QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id", userId.intValue());
                queryWrapper1.eq("password", oldPwd);
                Admin admin = adminService.getOne(queryWrapper1);
                if (admin != null || "".equals(admin)) {
                    // 修改
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                } else {
                    return Result.fail().message("原密码有误!");
                }
                break;
            case 2:
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id", userId.intValue());
                queryWrapper2.eq("password", oldPwd);
                Student student = studentService.getOne(queryWrapper2);
                if (student != null || "".equals(student)) {
                    // 修改
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                } else {
                    return Result.fail().message("原密码有误!");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("id", userId.intValue());
                queryWrapper3.eq("password", oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);
                if (teacher != null || "".equals(teacher)) {
                    // 修改
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                } else {
                    return Result.fail().message("原密码有误!");
                }
                break;
        }

        return Result.ok();
    }

    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("要上传的文件") @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ) {
        //使用UUID随机生成文件名
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        //生成新的文件名字
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String newFileName = uuid.concat(originalFilename.substring(i));

        // 保存文件 (将文件发送到第三方/独立的图片服务器上)
        String portraitPath = "D:/zhxy/target/classes/public/upload/".concat(newFileName);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 响应图片路径
        String path = "upload/".concat(newFileName);
        return Result.ok(path);
    }


    @ApiOperation("通过token获取用户信息")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token) {
        // 获取用户中请求的token
        // 检查token 是否过期 20H
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        // 从token中解析出 用户id 和 用户的类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        // 准备一个map用户存放相应的数据
        Map<String, Object> map = new HashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType", 1);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType", 2);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType", 3);
                map.put("user", teacher);
                break;
        }
        return Result.ok(map);
    }

    @ApiOperation("登录请求验证")
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        // 验证码校验
        HttpSession session = request.getSession();
        String systemVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        // session过期,验证码超时
        if ("".equals(systemVerifiCode) || null == systemVerifiCode) {
            return Result.fail().message("验证码失效,请刷新后重试");
        }
        // 验证码有误
        if (!loginVerifiCode.equalsIgnoreCase(systemVerifiCode)) {
            return Result.fail().message("验证码有误,请刷新后重新输入");
        }
        // 从session域中已拆除现有验证码
        session.removeAttribute("verifiCode");


        // 准备一个map用户存放相应的数据
        Map<String, Object> map = new HashMap<>();
        // 分用户类型进行校验
        switch (loginForm.getUserType()) {
            case 1:// 管理员身份
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        // 用户的类型和用户id转换成一个密文, 以token的名称向客户端反馈
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:// 学生身份
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        // 用户的类型和用户id转换成一个密文, 以token的名称向客户端反馈
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:// 教师身份
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher) {
                        // 用户的类型和用户id转换成一个密文, 以token的名称向客户端反馈
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此用户");
    }

    @ApiOperation("获取验证码图片")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码文本放入session域, 为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);
        //将验证码图片相应给浏览器
        try {
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
