package com.tongji.software_management.service;

import com.tongji.software_management.dao.*;
import com.tongji.software_management.entity.DBEntity.*;
import com.tongji.software_management.entity.LogicalEntity.ClassInfo;
import com.tongji.software_management.entity.LogicalEntity.User;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.beans.Expression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Resource
    private AttendRepository attendRepository;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private InstructorRepository instructorRepository;
    @Resource
    private AdministratorRepository administratorRepository;
    @Resource
    private ExpReportRepository expReportRepository;
    @Resource
    private CourseExpRepository courseExpRepository;
    @Resource
    private ExpScoreRepository expScoreRepository;
    @Resource
    private ReferenceRepository referenceRepository;
    @Resource
    private TeachesRepository teachesRepository;
    @Resource
    private TakesRepository takesRepository;
    @Resource
    private AttendScoreRepository attendScoreRepository;
    @Resource
    private ExperimentRepository experimentRepository;

    public Object ExistEmail(String email) {
        Student student = studentRepository.findStudentByEmail(email);
        if(student == null){
            Instructor instructor = instructorRepository.findInstructorByEmail(email);
            if(instructor == null){
                //三个表都不存在时才返回“不存在”
                return administratorRepository.findAdministratorByEmail(email);
            }
            return instructor;
        }
        return student;
    }

    public User ifActivated(String userNumber) {
        Student student = studentRepository.findStudentByStudentNumber(userNumber);
        if(student == null){
            Instructor instructor = instructorRepository.findInstructorByInstructorNumber(userNumber);
            if(instructor == null){
                return administratorRepository.findAdministratorByAdminNumber(userNumber);
            }
            else {
                return instructor;
            }
        }
        else{
            return student;
        }
    }

    public int alterInstructorInformation(String instructorNumber, String email, String phoneNumber) {
        return instructorRepository.updateInstructor(instructorNumber, email, phoneNumber);
    }

    public int alterInstructorInformation(String instructorNumber, String email, String name, Integer sex, String phoneNumber) {
        return instructorRepository.updateInstructor(instructorNumber, email, name, sex, phoneNumber);
    }

    public int alterStudentInformation(String studentNumber, String email, String name, Integer sex, String phoneNumber) {
        return studentRepository.updateStudent(studentNumber, email, name, sex, phoneNumber);
    }

    public int alterStudentInformation(String studentNumber, String email, String phoneNumber) {
        return studentRepository.updateStudent(studentNumber, email, phoneNumber);
    }

    public int alterUserInfo(String identity, String userNumber, String phoneNumber, String email) {
        int result = 0;

        if(identity.equals("student")){
            //修改者是“学生”
            result = alterStudentInformation(userNumber,email,phoneNumber);
        }else if(identity.equals("instructor")){
            //修改者是“教师”
            result = alterInstructorInformation(userNumber,email,phoneNumber);
        }

        return result;
    }

    public String getPassword(String identity, String userNumber) {
        User user = null;
        if(identity.equals("student")){
            user = studentRepository.findStudentByStudentNumber(userNumber);
        }else if(identity.equals("instructor")){
            user = instructorRepository.findInstructorByInstructorNumber(userNumber);
        }else{
            user = administratorRepository.findAdministratorByAdminNumber(userNumber);
        }
        return user.getPassword();
    }

    public int changePassword(String identity, String userNumber, String newPassword) {
        if(identity.equals("student")){
            return studentRepository.UpdatePasswordByStudentNumber(userNumber,newPassword);
        }else if(identity.equals("instructor")){
            return instructorRepository.UpdatePasswordByInstructorNumber(userNumber,newPassword);
        }else{
            return administratorRepository.UpdatePasswordByAdminNumber(userNumber,newPassword);
        }
    }

    public void activateAccount(String identity, String email) {
        if(identity.equals("student")){
            studentRepository.SetStatus(email,1);
        }else if(identity.equals("instructor")){
            instructorRepository.SetStatus(email,1);
        }
    }

    public List<Map<String, String>> getExpReports(String courseID, String classID) {
        List<Map<String,String>> expReportInfoList =  new ArrayList<>();
        //获取原生信息
        List<ExpReport> expReportList = expReportRepository.findExpReportsByCourseIdAndClassId(courseID, classID);
        //进行字段加工处理
        for(ExpReport expReport : expReportList){
            Map<String,String> map = new HashMap<>();
            map.put("reportName",expReport.getReportName());
            map.put("reportDescription",expReport.getReportInfo());
            map.put("startDate",expReport.getStartDate());
            map.put("endDate",expReport.getEndDate());
            map.put("reportType",expReport.getFileType());
            map.put("expName",expReport.getExpname());
            //获取实验的成绩占比
            CourseExp courseExp = courseExpRepository.findCourseExpByCourseIdAndExpname(expReport.getCourseId(),expReport.getExpname());
            map.put("weight",courseExp.getPercent()+"%");
            expReportInfoList.add(map);
        }
        return expReportInfoList;
    }

    public List<ExpScore> getExpScoresOfExpname(String courseID, String classID, String expname) {

        //找到提交作业的学生记录
        return expScoreRepository.findExpScoresByCourseIdAndExpnameAndClassId(courseID,expname,classID);
    }

    public List<Reference> getReferencesOfSection(String courseID, String classID) {
        //该课程下的所有参考资料
        return referenceRepository.findReferencesByCourseIdAndClassId(courseID,classID);
    }

    public ClassInfo getClassInfo(String courseID, String classID) {
        ClassInfo classInfo = new ClassInfo();
        List<Teaches> teachesList = teachesRepository.findTeachesByCourseIdAndClassId(courseID, classID);
        for (Teaches teaches : teachesList) {
            String instructorNumber = teaches.getInstructorNumber();
            Instructor instructor = instructorRepository.findInstructorByInstructorNumber(instructorNumber);
            classInfo.addInstructor(instructor);
        }

        List<Takes> takesList = takesRepository.findTakesByCourseIdAndClassId(courseID, classID);
        for (Takes takes : takesList) {
            String studentNumber = takes.getStudentNumber();
            int status = takes.getStatus();//0,1
            Student person = studentRepository.findStudentByStudentNumber(studentNumber);
            if (status == 0) {
                //学生
                classInfo.addStudent(person);
            }
            else if (status == 1) {
                //助教
                classInfo.addAssistant(person);
            }
        }
        return classInfo;
    }

    public List<Attend> getAttendInfo(String courseID, String classID) {
        return attendRepository.findAttendsByCourseIdAndClassId(courseID, classID);
    }

    public List<AttendScore> getAttendScoreByCourseIDAndClassIDAndStudentNumber(String courseID, String classID, String studentNumber) {
        return attendScoreRepository.findAttendScoreByCourseIdAndClassIdAndStudentNumber(courseID, classID, studentNumber);
    }

    public Attend getAttendByCourseIDAndClassIDAndTitle(String courseID, String classID, String title) {
        return attendRepository.findAttendByCourseIdAndClassIdAndTitle(courseID, classID, title);
    }

    public Boolean judgeAttendScoreIfExist(String courseID, String classID, String title, String studentNumber) {
        AttendScore attendScore = attendScoreRepository.findAttendScoreByCourseIdAndClassIdAndTitleAndStudentNumber(courseID, classID, title, studentNumber);
        return attendScore != null;
    }

    public List<Experiment> getExperimentListByCourseIDAndClassID(String courseID, String classID) {
        return experimentRepository.findExperimentsByCourseIdAndClassId(courseID,classID);
    }

    public List<ExpReport> getExpReportListByCourseIDAndClassID(String courseID, String classID) {
        return expReportRepository.findExpReportsByCourseIdAndClassId(courseID,classID);
    }


}
