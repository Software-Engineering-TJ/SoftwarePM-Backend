package com.tongji.software_management.service;

import com.tongji.software_management.dao.AttendRepository;
import com.tongji.software_management.dao.StudentRepository;
import com.tongji.software_management.entity.DBEntity.Attend;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private AttendRepository attendRepository;
    @Resource
    private StudentRepository studentRepository;

    public List<Attend> getAttendInfo(String courseID, String classID) {
        return attendRepository.findAttendsByCourseIdAndClassId(courseID, classID);
    }

    public int alterStudentInformation(String studentNumber, String email, String name, Integer sex, String phoneNumber) {
        return studentRepository.updateStudent(studentNumber, email, name, sex, phoneNumber);
    }



}
