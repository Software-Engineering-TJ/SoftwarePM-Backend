package com.tongji.software_management.controller;

import com.alibaba.fastjson.JSONObject;
import com.tongji.software_management.entity.DBEntity.Attend;
import com.tongji.software_management.entity.LogicalEntity.ApiResult;
import com.tongji.software_management.service.UserService;
import com.tongji.software_management.utils.ApiResultHandler;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/")
public class PersonalController {
    @Resource
    protected UserService userService;

    @PostMapping("/")
    public ApiResult alterStudentInformation(@RequestBody JSONObject req) {
        String studentNumber = (String) req.get("studentNumber"); //主码
        String email = (String) req.get("email");
        String name = (String) req.get("name");
        Integer sex = (Integer) req.get("sex");
        String phoneNumber = (String) req.get("phoneNumber");
        int msg = userService.alterStudentInformation(studentNumber, email, name, sex, phoneNumber);
        return ApiResultHandler.buildApiResult(200,"",msg);
    }

    @PostMapping("/")
    public ApiResult alterInstructorInformation(@RequestBody JSONObject req) {
        String instructorNumber = "1"; //主码
        String email = "11";
        String password = (String) req.get("password");
        int sex = 1;
        String phoneNumber = (String) req.get("phoneNumber");
        int msg = userService.alterInstructorInformation(instructorNumber, email, password, sex, phoneNumber);
        return ApiResultHandler.buildApiResult(200,"",msg);
    }

}
