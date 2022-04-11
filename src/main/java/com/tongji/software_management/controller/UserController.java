package com.tongji.software_management.controller;

import com.alibaba.fastjson.JSONObject;
import com.tongji.software_management.entity.DBEntity.Instructor;
import com.tongji.software_management.entity.DBEntity.Student;
import com.tongji.software_management.entity.LogicalEntity.ApiResult;
import com.tongji.software_management.entity.LogicalEntity.User;
import com.tongji.software_management.service.AdministratorService;
import com.tongji.software_management.service.InstructorService;
import com.tongji.software_management.service.StudentService;
import com.tongji.software_management.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private AdministratorService administrationService;
    @Resource
    private StudentService studentService;
    @Resource
    private InstructorService instructorService;


}
