package com.tongji.software_management.controller;

import com.alibaba.fastjson.JSONObject;
import com.tongji.software_management.dao.AttendRepository;
import com.tongji.software_management.dao.PracticeRepository;
import com.tongji.software_management.dao.PracticeScoreRepository;
import com.tongji.software_management.entity.DBEntity.*;
import com.tongji.software_management.entity.LogicalEntity.ApiResult;
import com.tongji.software_management.entity.LogicalEntity.ViewExperimentInfo;
import com.tongji.software_management.service.AdministratorService;
import com.tongji.software_management.service.InstructorService;
import com.tongji.software_management.service.StudentService;
import com.tongji.software_management.utils.ApiResultHandler;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StudentController {
    @Resource
    AdministratorService administrationService;
    @Resource
    StudentService studentService;
    @Resource
    InstructorService instructorService;
    @Resource
    AttendRepository attendRepository;
    @Resource
    PracticeScoreRepository practiceScoreRepository;
    @Resource
    PracticeRepository practiceRepository;

    @PostMapping("")
    public ApiResult viewExperiment(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");

        List<Experiment> experimentList = studentService.getExperimentByCourseIDAndClassID(courseID, classID);
        List<ViewExperimentInfo> viewExperimentInfoList = new ArrayList<ViewExperimentInfo>();

        for (Experiment experiment : experimentList) {
            ViewExperimentInfo viewExperimentInfo = new ViewExperimentInfo();

            String expname = experiment.getExpname();
            viewExperimentInfo.setExpName(expname);
            viewExperimentInfo.setStartDate(experiment.getStartDate());
            viewExperimentInfo.setEndDate(experiment.getEndDate());
            viewExperimentInfo.setExpInfo(experiment.getExpInfo());

            CourseExp courseExp = studentService.getCourseExpByCourseIDAndExpname(courseID, expname);
            viewExperimentInfo.setPriority(courseExp.getPriority());
            viewExperimentInfo.setDifficulty(courseExp.getDifficulty());
            viewExperimentInfo.setWeight(courseExp.getPercent());

            viewExperimentInfoList.add(viewExperimentInfo);
        }

        return ApiResultHandler.buildApiResult(200,"",viewExperimentInfoList);
    }

    @PostMapping("")
    public ApiResult getTakes(@RequestBody JSONObject reqObject) {

        String studentNumber = (String) reqObject.get("studentNumber");
        //获取学生——课程详细信息
        Map<String, Object> map = administrationService.GetTakesInfoByStudentNumber(studentNumber);
        //返回必要信息:这里直接用了管理员获取学生课程的方法，所以信息量足够，前端可以根据需要读取
        Map<String, Object> takes = new HashMap<>();
        takes.put("sections", map.get("sectionInformation"));

        return ApiResultHandler.buildApiResult(200,"",takes);
    }

    @PostMapping("")
    public ApiResult getCourseNotice(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");

        //获取需要的字段
        List<Notice> notices = studentService.getCourseNotice(courseID,classID);
        //抽取前端需要的通知属性
        List<Map<String,String>> noticesInfo = new ArrayList<>();
        for(Notice n : notices){
            Map<String,String> map = new HashMap<>();
            map.put("title",n.getTitle());
            map.put("content",n.getContent());
            map.put("date",n.getDate());
            //获取老师信息
            String instructorNumber = n.getInstructorNumber();
            Map<String,Object> instructorInfo = administrationService.SearchInstructorByInstructorNumber(instructorNumber);
            map.put("issuer", (String) instructorInfo.get("name"));
            //将该条信息加入到List
            noticesInfo.add(map);
        }

        return ApiResultHandler.buildApiResult(200,"",noticesInfo);
    }

    @PostMapping("")
    public ApiResult getGrade(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String expname = (String) reqObject.get("expName");
        String studentNumber = (String) reqObject.get("studentNumber");
        //获取成绩，score可能是”助教尚未批改“or”报告尚未提交“or正常成绩
        ExpScore expScore = studentService.getExpScore(courseID,classID,expname,studentNumber);

        String score = "";
        String comment= "";
        if(expScore == null){
            //数据库中没有找到，说明没有提交作业
            score = "报告尚未提交";
            comment = "报告未提交，请先提交报告";
        }else{
            if(expScore.getScore()==-1) {
                //"-1"意味着报告尚未批改
                score = "助教尚未批改";
                comment = "助教还没有来得及批改，请耐心等待^-^";
            }else{
                //成绩正常
                score = Double.toString(expScore.getScore());
                comment = expScore.getComment();
            }
        }
        Map<String,String> map = new HashMap<>();
        map.put("grade",score);
        map.put("comment",comment);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("")
    public ApiResult getDuty(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String studentNumber = (String) reqObject.get("studentNumber");

        Map<String,String> map = new HashMap<>();
        map.put("Duty",studentService.getDuty(courseID,classID,studentNumber));

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("")
    public ApiResult signIn(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String title = (String) reqObject.get("AttendanceName");
        String studentNumber = (String) reqObject.get("studentNumber");
        //学生当前签到的时间
        Date currentTime = new Date();
        //考勤截止日期
        Date endTime = null;
//        AttendDao attendDao = new AttendDaoImpl();
        Attend attend = attendRepository.findAttendByCourseIdAndClassIdAndTitle(courseID,classID,title);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            endTime = simpleDateFormat.parse(attend.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //比较两个时间
        int onTime = 0;
        if(currentTime.before(endTime)){
            onTime = 1;
        }
        //添加考勤记录
        Map<String,Object> map = new HashMap<>();
        if(studentService.addAttendScore(courseID,classID,title,studentNumber,onTime)==1){
            map.put("result",1);
        }else{
            map.put("result",0);
        }
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("")
    public ApiResult getWeightOfGrade(@RequestBody JSONObject reqObject) {

        String courseId = (String) reqObject.get("courseID");
        List<CourseExp> courseExpList = studentService.getCoursesByCourseID(courseId);

        return ApiResultHandler.buildApiResult(200,"",courseExpList);
    }

    //学生获取自己的实验成绩 √
    @PostMapping("")
    public ApiResult getExpGrades(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String studentNumber = (String) reqObject.get("studentNumber");

        //先查看都有哪些已发布的实验
        List<Experiment> experimentList = studentService.getExperimentByCourseIDAndClassID(courseID,classID);
        //成绩和排名信息
        List<Map<String, Object>> expGradeInfoList = new ArrayList<>();
        if(experimentList != null){
            for(Experiment experiment : experimentList){
                Map<String, Object> map = studentService.getGradeAndRankingOfExperiment(experiment,studentNumber);
                expGradeInfoList.add(map);
            }
        }

        return ApiResultHandler.buildApiResult(200,"",expGradeInfoList);
    }

    //学生获取自己某门课程的总成绩
    @PostMapping("")
    public ApiResult getTotalGrade(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String studentNumber = (String) reqObject.get("studentNumber");

        float attendGrade = studentService.getGradeOfAttendance(courseID,classID,studentNumber);
        float practiceGrade = studentService.getGradeOfPractice(courseID,classID,studentNumber);
        float expGrade = studentService.getGradeOfExperiment(courseID,classID,studentNumber);

        float totalGrade = attendGrade+practiceGrade+expGrade;

        Map<String,Float> map = new HashMap<>();
        map.put("attendScore",attendGrade);
        map.put("practiceScore",practiceGrade);
        map.put("expScore",expGrade);
        map.put("totalScore",totalGrade);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    //学生查看自己参加的对抗练习的成绩
    @PostMapping("")
    public ApiResult viewPracticeStu(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String studentNumber = (String) reqObject.get("studentNumber");

        //需要返回的信息列表
        List<Map<String, Object>> practiceInfoList = new ArrayList<>();

        //该班级发布的所有对抗练习
        List<Practice> practiceList = practiceRepository.findPracticesByCourseIdAndClassId(courseID,classID);

        //查找每一个对抗练习中该学生的小组排名，确定成绩
        for(Practice practice : practiceList){
            Map<String, Object> map = new HashMap<>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("practiceName",practice.getPracticeName());
            map.put("startTime",dateFormat.format(practice.getStartTime()));
            map.put("endTime",dateFormat.format(practice.getEndTime()));
            //当前时间
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if(now.before(practice.getEndTime())){
                map.put("status","正在进行");
            }else if(now.after(practice.getEndTime())){
                map.put("status","已结束");
            }else{
                map.put("status","尚未开始");
            }

            float grade = 0;
            //该学生再本次对抗练习中的成绩信息
            PracticeScore practiceScore = practiceScoreRepository.findPracticeScoreByCourseIdAndClassIdAndPracticeNameAndStudentNumber(courseID,classID,practice.getPracticeName(),studentNumber);
            if(practiceScore == null){
                //没有参加该次对抗练习，成绩为0
                map.put("grade",grade);
                practiceInfoList.add(map);
                continue;
            }
            //排序后的小组成员
            List<PracticeScore> practiceScoreList = practiceScoreRepository.findPracticeScoreByGroup(courseID,classID,practice.getPracticeName(), practiceScore.getGroupNumber());
            //算成绩
            if(practiceScoreList != null){
                for(int i=0;i<practiceScoreList.size();i++){
                    if(practiceScoreList.get(i).getStudentNumber().equals(practiceScore.getStudentNumber())){
                        //找到所在的组的名次i(第一名100，第二名60，第三名20)
                        grade = ((3-i)*2-1)*20;
                        map.put("grade",grade);
                        practiceInfoList.add(map);
                        break;
                    }
                }
            }
        }

        return ApiResultHandler.buildApiResult(200,"",practiceInfoList);
    }

    //学生写课程反馈
    @PostMapping("")
    public ApiResult writeReflection(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String studentNumber = (String) reqObject.get("studentNumber");
        String content = (String) reqObject.get("content");

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date);

        if(studentService.writeReflection(courseID,classID,studentNumber,content,dateStr)==1) {
            return ApiResultHandler.buildApiResult(200,"反馈成功",null);
        }
        return ApiResultHandler.buildApiResult(201,"反馈失败，请重试",null);
    }
}
