package com.tongji.software_management.service;

import com.tongji.software_management.dao.*;
import com.tongji.software_management.entity.DBEntity.*;
import com.tongji.software_management.entity.LogicalEntity.SectionInformation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class AdministratorService {
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private InstructorRepository instructorRepository;
    @Resource
    private AdministratorRepository administratorRepository;
    @Resource
    private TakesRepository takesRepository;
    @Resource
    private TeachesRepository teachesRepository;
    @Resource
    private CourseRepository courseRepository;
    @Resource
    private SectionRepository sectionRepository;
    @Resource
    private CourseExpRepository courseExpRepository;

    public Student getStudentByStudentNumber(String studentNumber) {
        return studentRepository.findStudentByStudentNumber(studentNumber);
    }

    public Administrator getAdministrationInfo(String adminNumber) {
        return administratorRepository.findAdministratorByAdminNumber(adminNumber);
    }

    public List<Course> getCourseAppliedList() {
        return courseRepository.findCoursesByFlag(0);
    }

    public Instructor getInstructorByInstructorNumber(String instructorNumber) {
        return instructorRepository.findInstructorByInstructorNumber(instructorNumber);
    }

    public void aduitCourse(String courseID, String result) {
        if("yes".equals(result)){
            courseRepository.UpdateFlagOfCourseByCourseID(courseID,1);
        }else{
            //1.先删试验大纲
            courseExpRepository.deleteCourseExpByCourseId(courseID);
            //2.再删课程
            courseRepository.deleteCourseByCourseId(courseID);
        }
    }

    public boolean EmailExists(String email) {
        Student student = studentRepository.findStudentByEmail(email);
        if(student != null){
            return true;
        }else{
            Instructor instructor = instructorRepository.findInstructorByEmail(email);
            if(instructor != null){
                return true;
            }else{
                Administrator administrator = administratorRepository.findAdministratorByEmail(email);
                return administrator != null;
            }
        }
    }

    public String AddStudent(String studentNumber,String email,String name,String phoneNumber,int sex) {
        //1.先检查Email是否重复
        if(EmailExists(email)){
            return "The Email has been existed!";
        }
        //2.email没问题再插入学生信息
//        studentRepository.save(Student.builder()
//                .studentNumber(studentNumber)
//                .email(email)
//                .name(name)
//                .phoneNumber(phoneNumber)
//                .sex(sex)
//                .build());
        return "success";  //如果没有任何意外，msg为"success"
    }

    public Map<String, Object> GetTakesInfoByStudentNumber(String studentNumber) {

        Map<String,Object> map = new HashMap<>();
        //存储课程有关信息的集合
        List<SectionInformation> informationList = new ArrayList<>();

        //获取学生姓名和学号，并加入到map中
        Student student = studentRepository.findStudentByStudentNumber(studentNumber);
        map.put("name",student.getName());
        map.put("studentNumber",studentNumber);

        //接着查询takes表中和该学号相关的表
        List<Takes> takesList = takesRepository.findTakesByStudentNumber(studentNumber);
        Iterator<Takes> iterator = takesList.iterator();
        //遍历该学生所有课程
        while(iterator.hasNext()){
            Takes takes = iterator.next();
            SectionInformation information = new SectionInformation();
            //确定学生职责
            information.setDuty((takes.getStatus()==0)?"学生":"助教");
            //确定班级号和课程号
            information.setCourseID(takes.getCourseId());
            information.setClassID(takes.getClassId());
            //确定课程名字和责任教师名
            Course course = courseRepository.findCourseByCourseId(takes.getCourseId());
            if(course.getFlag()==-1){
                //课程无效
                continue;
            }
            information.setTitle(course.getTitle());
            Instructor instructor = instructorRepository.findInstructorByInstructorNumber(course.getInstructorNumber());
            information.setChargingTeacher(instructor.getName());
            //确定课程时间
            Section section = sectionRepository.findSectionByCourseIdAndClassId(takes.getCourseId(),takes.getClassId());
            information.setDay("星期"+section.getDay());
            information.setTime("第"+section.getDay()+"节课");
            //确定教师名（可能有多个任课老师，就像一些选修课，当然概率很低，但考虑到实际情况，逻辑上尽量完善吧）
            StringBuilder allTeachers;  //所有老师名字，用","隔开
            List<Teaches> teachesList = teachesRepository.findTeachesByCourseIdAndClassId(takes.getCourseId(),takes.getClassId());
            Iterator<Teaches> iterator1 = teachesList.iterator();
            Instructor firstInstructor = instructorRepository.findInstructorByInstructorNumber(iterator1.next().getInstructorNumber());
            allTeachers = new StringBuilder(firstInstructor.getName());
            while(iterator1.hasNext()){
                Instructor nextInstructor = instructorRepository.findInstructorByInstructorNumber(iterator1.next().getInstructorNumber());
                allTeachers.append(",").append(nextInstructor.getName());
            }
            information.setTeacher(allTeachers.toString());
            informationList.add(information);
        }
        //informationList可以不用转换成数组而加入到map，因为底层实现本就是数组
        map.put("sectionInformation",informationList);
        return map;
    }

    public Map<String, Object> GetTeachesInfoByInstructorNumber(String instructorNumber) {

        Map<String,Object> map = new HashMap<>();
        List<SectionInformation> informationList = new ArrayList<>();

        //获取老师的名字和学号
        Instructor instructor = instructorRepository.findInstructorByInstructorNumber(instructorNumber);
        map.put("name",instructor.getName());
        map.put("instructorNumber",instructorNumber);
        //教师教授的所有课程
        List<Teaches> teachesList = teachesRepository.findTeachesByInstructorNumber(instructorNumber);
        //开始遍历课程
        for (Teaches teaches : teachesList) {
            SectionInformation sectionInformation = new SectionInformation();
            //获取课程名称
            Course course = courseRepository.findCourseByCourseId(teaches.getCourseId());
            if (course.getFlag() == -1) {
                //课程无效
                continue;
            }
            sectionInformation.setTitle(course.getTitle());
            //获取班级号和课程号
            sectionInformation.setCourseID(teaches.getCourseId());
            sectionInformation.setClassID(teaches.getClassId());


            //获取任课身份
            if (course.getInstructorNumber().equals(instructorNumber)) {
                sectionInformation.setDuty("教师and责任教师");
            } else {
                sectionInformation.setDuty("教师");
            }
            //获取上课时间
            Section section = sectionRepository.findSectionByCourseIdAndClassId(teaches.getCourseId(), teaches.getClassId());
            sectionInformation.setDay("星期" + section.getDay());
            sectionInformation.setTime("第" + section.getTime() + "节课");

            //将课程相关信息加入到列表
            informationList.add(sectionInformation);
        }
        //老师管理的course（可能不在teachesList中）
        List<Course> courseList = courseRepository.findCourseByInstructorNumber(instructorNumber);
        if(courseList != null){
            for(Course course : courseList){
                if(course.getFlag()==-1){
                    //课程已无效
                    continue;
                }
                String courseID = course.getCourseId();
                boolean flag = true;
                for(Teaches teaches : teachesList){
                    //如果匹配上了就说明不必添加该课程信息了
                    if(courseID.equals(teaches.getCourseId())){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    SectionInformation sectionInformation = new SectionInformation();
                    sectionInformation.setCourseID(courseID);
                    sectionInformation.setDuty("责任教师");
                    sectionInformation.setTitle(course.getTitle());

                    //将课程相关信息加入到列表
                    informationList.add(sectionInformation);
                }
            }
        }
        //将任课列表加入到map
        map.put("sectionInformation",informationList);
        return map;
    }

    public String ChangeStudentDuty(String studentNumber, String courseID, String classID,String duty) {
        String msg = "changing duty failed!";  //用于记录修改结果是否成功的信息
        Integer newDuty = ("学生".equals(duty))?1:0; //映射到数据库的身份表示形式
        if(takesRepository.SetDuty(studentNumber,courseID,classID,newDuty)==1){
            //修改成功
            msg = "success";
        }
        return msg;
    }

    public boolean DeleteStudent(String email) {
        //??
        return false;
    }

    public String AddInstructor(String instructorNumber,String email,String name,String phoneNumber,int sex) {
        String msg = null;  //用于记录添加结果是否成功的信息
        //1.先检查Email是否重复
        if(EmailExists(email)){
            return "The Email has been existed!";
        }
        //2.email没问题再插入教师信息
//        instructorRepository.save(Instructor.builder()
//                .instructorNumber(instructorNumber)
//                .email(email)
//                .name(name)
//                .phoneNumber(phoneNumber)
//                .sex(sex)
//                .build());

        return "success";  //如果没有任何意外，msg为"success"
    }

    public Map<String,Object> SearchInstructorByInstructorNumber(String instructorNumber) {
        Map<String,Object> map = new HashMap<>();

        Instructor instructor = instructorRepository.findInstructorByInstructorNumber(instructorNumber);
        if(instructor != null){
            map.put("instructorNumber",instructorNumber);
            map.put("name",instructor.getName());
            map.put("sex",instructor.getSex());
            map.put("phoneNumber",instructor.getPhoneNumber());
            map.put("email",instructor.getEmail());
        }
        return map;
    }

    public Map<String,Object> CheckTeacherDuty(String courseID) {
        Course course = courseRepository.findCourseByCourseId(courseID);
        //课程的责任教师工号
        String instructorNumber = course.getInstructorNumber();
        //获取教师名
        Instructor instructor = instructorRepository.findInstructorByInstructorNumber(instructorNumber);
        String name = instructor.getName();
        //加入到map
        Map<String,Object> map = new HashMap<>();
        map.put("instructorNumber",instructorNumber);
        map.put("name",name);
        return map;
    }

    public String ChangeDutyInstructor(String instructorNumber, String courseID) {
        String msg = "success";  //修改结果
        //修改责任教师
        if(courseRepository.SetDutyInstructor(courseID,instructorNumber)!=1){
            msg = "error";
        }
        return msg;
    }

    public boolean DeleteInstructor(String email) {
        return false;
    }

    public List<Takes> SearchTakesOfStudent(String email) {
        return null;
    }

    public List<Teaches> SearchTeachesOfInstructor(String email) {
        return null;
    }

    public boolean SetStudentStatus(String email, String courseID, String classID, int status) {
        return false;
    }

    public boolean SetInstructorStatus(String email, String courseID, String classID, int status) {
        return false;
    }
}
