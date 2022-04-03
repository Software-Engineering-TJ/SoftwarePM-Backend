package com.tongji.software_management.controller;

import com.alibaba.fastjson.JSONObject;
import com.tongji.software_management.entity.DBEntity.Attend;
import com.tongji.software_management.entity.LogicalEntity.ApiResult;
import com.tongji.software_management.service.UserService;
import com.tongji.software_management.utils.ApiResultHandler;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/")
public class PersonalController {
    @Resource
    UserService userService;

    @PostMapping("/")
    public ApiResult alterStudentInformation(@RequestBody JSONObject req) {
        String studentNumber = (String) req.get("studentNumber"); //主码
        String email = (String) req.get("email");
        String password = (String) req.get("password");
        int sex = (int) req.get("sex");
        String phoneNumber = (String) req.get("phoneNumber");
        System.out.println(req);
        int msg = userService.alterStudentInformation(studentNumber, email, password, sex, phoneNumber);
        return ApiResultHandler.buildApiResult(200,"", msg);
    }


//    @PostMapping("/t")
//    public ApiResult Test(@RequestBody JSONObject jsonObject) {
//        System.out.println(jsonObject);
//        String courseId = (String) jsonObject.get("courseId");
//        String classId = (String) jsonObject.get("classId");
//        System.out.println(courseId+classId);
//        List<Attend> attendList = userService.getAttendInfo(courseId, classId);
//        return ApiResultHandler.buildApiResult(200,"", attendList);
//    }

}
