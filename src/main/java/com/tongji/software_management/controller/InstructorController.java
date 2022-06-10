package com.tongji.software_management.controller;

import com.alibaba.fastjson.JSONObject;
import com.tongji.software_management.dao.ChoiceQuestionRepository;
import com.tongji.software_management.dao.CourseRepository;
import com.tongji.software_management.entity.DBEntity.*;
import com.tongji.software_management.entity.LogicalEntity.ApiResult;
import com.tongji.software_management.entity.LogicalEntity.StudentAttendanceInfo;
import com.tongji.software_management.service.AdministratorService;
import com.tongji.software_management.service.InstructorService;
import com.tongji.software_management.service.PracticeService;
import com.tongji.software_management.utils.ApiResultHandler;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags="教师模块")
@CrossOrigin
@RestController
@RequestMapping("/instructor")
public class InstructorController {
    @Resource
    InstructorService instructorService;
    @Resource
    AdministratorService administratorService;
    @Resource
    ChoiceQuestionRepository choiceQuestionRepository;
    @Resource
    CourseRepository courseRepository;
    @Resource
    PracticeService practiceService;

    //获取该考勤已经签到和未签到的学生，要做表的not in操作
    @PostMapping("viewAttendance")
    public ApiResult viewAttendance(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String title = (String) reqObject.get("AttendanceName");

        List<AttendScore> attendScoreList = instructorService.getAttendScoreByCourseIDAndClassIDAndTitle(courseID, classID, title);
        StudentAttendanceInfo studentAttendanceInfo = new StudentAttendanceInfo();

        //能在attendScoreList里的都是已签到的，未签到的在整个班级里除去即可

        for (AttendScore attendScore : attendScoreList) {
            String studentNumber = attendScore.getStudentNumber();
            String studentName = instructorService.getStudentByStudentNumber(studentNumber).getName();
            studentAttendanceInfo.addSubmitted(studentNumber, studentName);
        }

        //未签到的
//        List<Takes> takesList = instructorService.getTakesNotInAttendScore(courseID, classID, title);
//        for (Takes takes : takesList) {
//            String studentNumber = takes.getStudentNumber();
//            String studentName = instructorService.getStudentByStudentNumber(studentNumber).getName();
//            studentAttendanceInfo.addUnSubmitted(studentNumber, studentName);
//        }

        return ApiResultHandler.buildApiResult(200,"",studentAttendanceInfo);
    }

    //教师发布对抗练习，将题目列表交给PracticeServer
    @PostMapping("createQuestionList")
    public ApiResult createQuestionList(@RequestBody JSONObject reqObject) {
        int size = (int) reqObject.get("size");
        List<ChoiceQuestion> choiceQuestionList = instructorService.getRandomQuestionList(size);

        return ApiResultHandler.buildApiResult(200,"",choiceQuestionList);
    }

    @PostMapping("getSections")
    public ApiResult getSections(@RequestBody JSONObject reqObject) {

        String instructorNumber = (String) reqObject.get("instructorNumber");
        List<Map<String,String>> map = instructorService.GetSections(instructorNumber);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("getExperimentInfo")
    public ApiResult getExperimentInfo(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        List<Map<String,String>> map = instructorService.GetCourseExpInfo(courseID,classID);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("releaseExperiment")
    public ApiResult releaseExperiment(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String expname = (String) reqObject.get("expName");
        String endDate = (String) reqObject.get("endDate");
        String expInfo = (String) reqObject.get("expInfo");
        //实验发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String startDate = simpleDateFormat.format(new Date());

        Map<String,Integer> map = new HashMap<>();
        int result = 0;
        if(instructorService.ReleaseExperiment(courseID,expname,classID,startDate,endDate,expInfo)==1){
            //发布实验成功
            result = 1;
        }
        map.put("result",result);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("examineExperimentInfo")
    public ApiResult examineExperimentInfo(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String expname = (String) reqObject.get("expName");

        Map<String,String> map = instructorService.ExamineExperimentInfo(courseID,classID,expname);
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("modifyExperimentInfo")
    public ApiResult modifyExperimentInfo(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String expname = (String) reqObject.get("expName");
        String newEndDate = (String) reqObject.get("newEndDate");
        String newExpInfo = (String) reqObject.get("newExpInfo");

        Map<String,Integer> map = new HashMap<>();
        int result = 0;
        if(instructorService.ModifyExperiment(courseID,classID,expname,newEndDate,newExpInfo)==1){
            //修改已发布实验的内容成功
            result = 1;
        }
        map.put("result",result);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("releaseNotice")
    public ApiResult releaseNotice(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String title = (String) reqObject.get("title");
        String content = (String) reqObject.get("content");
        String instructorNumber = (String) reqObject.get("issuer");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = new Date();
        String date= simpleDateFormat.format(startDate);

        Map<String,Integer> map = new HashMap<>();
        int result = 0;
        if(instructorService.ReleaseNotice(courseID,classID,instructorNumber,content,date,title)==1){
            //发布公告成功
            result = 1;
        }
        map.put("result",result);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("withdrawNotice")
    public ApiResult withdrawNotice(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String instructorNumber = (String) reqObject.get("instructorNumber");
        String date = (String) reqObject.get("date");

        Map<String,Integer> map = new HashMap<>();
        int result = 0;
        if(instructorService.DeleteNotice(courseID,classID,instructorNumber,date)==1){
            //删除公告成功
            result = 1;
        }
        map.put("result",result);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("releaseReportDesc")
    public ApiResult releaseReportDesc(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String expname = (String) reqObject.get("expName");
        Map<String,String> reportInfo = (Map<String, String>) reqObject.get("reportInfo");
        String reportName = reportInfo.get("reportName");
        String reportDescription = reportInfo.get("reportDescription");
        String startDate = reportInfo.get("startDate");
        String endDate = reportInfo.get("endDate");
        String fileType = reportInfo.get("reportType");

        Map<String,Integer> map = new HashMap<>();
        int result = 0;
        if(instructorService.ReleaseReportDesc(courseID,classID,expname,reportName,reportDescription,startDate,endDate,fileType)==1){
            //发布实验报告描述成功
            result = 1;
        }
        map.put("result",result);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("withdrawReportDesc")
    public ApiResult withdrawReportDesc(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String expname = (String) reqObject.get("expName");
        String reportName = (String) reqObject.get("reportName");

        Map<String,Integer> map = new HashMap<>();
        int result = 0;
        if(instructorService.DeleteReportDesc(courseID,classID,expname,reportName)==1){
            //撤回实验报告描述成功
            result = 1;
        }
        map.put("result",result);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("modifyReportDesc")
    public ApiResult modifyReportDesc(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String expname = (String) reqObject.get("expName");
        String newReportName = (String) reqObject.get("newReportName");
        String newEndDate = (String) reqObject.get("newEndDate");
        String newReportDescription = (String) reqObject.get("newReportDescription");
        String newFileType = (String) reqObject.get("newReportType");

        Map<String,Integer> map = new HashMap<>();
        int result = 0;
        if(instructorService.ModifyReportDesc(courseID,classID,expname,newReportName,newReportDescription,newEndDate,newFileType)==1){
            //修改实验报告描述成功
            result = 1;
        }
        map.put("result",result);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("getClassInfo")
    public ApiResult getClassInfo(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        List<Map<String,String>> map = instructorService.GetSectionInfoOfCourse(courseID);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("viewSubmission")
    public ApiResult viewSubmission(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String expname = (String) reqObject.get("expname");

        //提交记录
        List<ExpScore> expScoreList = instructorService.getSubmittedStudentList(courseID,classID,expname);
        //存学号
        List<String> studentSubmitList = new ArrayList<>();
        //存学号、姓名、成绩（-1表示没评分）
        List<Map<String,Object>> submitted = new ArrayList<>();
        List<Map<String,Object>> unSubmitted = new ArrayList<>();
        //班级所有学生的学号
        List<String> studentList = instructorService.getStudentNumbersByCourseIDAndClassID(courseID,classID);
        if(expScoreList != null){
            //有人交了
            for(ExpScore expScore : expScoreList){
                //已提交的学生
                String studentNumber = expScore.getStudentNumber();
                studentSubmitList.add(studentNumber);
                //存信息
                Student student = administratorService.getStudentByStudentNumber(studentNumber);
                Map<String,Object> map = new HashMap<>();
                map.put("studentNumber",studentNumber);
                map.put("studentName",student.getName());
                map.put("score",expScore.getScore());
                map.put("fileUrl",expScore.getFileUrl());
                submitted.add(map);
            }
            for(String studentNumber : studentList){
                if(!studentSubmitList.contains(studentNumber)){
                    //存信息
                    Student student = administratorService.getStudentByStudentNumber(studentNumber);
                    Map<String,Object> map = new HashMap<>();
                    map.put("studentNumber",studentNumber);
                    map.put("studentName",student.getName());
                    map.put("score",-1);
                    unSubmitted.add(map);
                }
            }
        }else{
            //都没交
            for(String studentNumber : studentList){
                //存信息
                Student student = administratorService.getStudentByStudentNumber(studentNumber);
                Map<String,Object> map = new HashMap<>();
                map.put("studentNumber",studentNumber);
                map.put("studentName",student.getName());
                map.put("score",-1);
                unSubmitted.add(map);
            }
        }

        Map<String,Object> map = new HashMap<>();
        map.put("submitted",submitted);
        map.put("unSubmitted",unSubmitted);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("releaseSignIn")
    public ApiResult releaseSignIn(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String attendanceName = (String) reqObject.get("attendanceName");
        String endTime = (String) reqObject.get("endTime");
        //签到发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = simpleDateFormat.format(new Date());

        Map<String,Object> map = new HashMap<>();
        //签到信息插入到数据库
        if(instructorService.addAttend(courseID,classID,attendanceName,startTime,endTime)==1){
            map.put("result",1);
            map.put("msg","考勤发布成功");
        }else{
            map.put("result",0);
            map.put("msg","考勤命名重复");
        }
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("addSection")
    public ApiResult addSection(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");
        String instructorNumber = (String) reqObject.get("instructorNumber");
        int day = (int) reqObject.get("day");
        int time = (int) reqObject.get("time");
        int number = (int) reqObject.get("number");
        if(administratorService.SearchInstructorByInstructorNumber(instructorNumber)==null){
            return ApiResultHandler.buildApiResult(200,"","添加课程失败");
        }
        int ret = instructorService.addSection(courseID,instructorNumber, day, time,number);
        return ApiResultHandler.buildApiResult(200,"",ret != -1 ? "添加课程成功" : "添加课程失败");
    }

    @PostMapping("addCourse")
    public ApiResult addCourse(@RequestBody JSONObject reqObject) {

        String title = (String)reqObject.get("title");
        String startDate = (String)reqObject.get("startDate");
        String endDate = (String)reqObject.get("endDate");
        String instructorNumber = (String)reqObject.get("instructorNumber");
        //实验信息
//        String experimentForm = (String) reqObject.get("experimentForm");
//        List<Map<String,Object>> courseExpInfoList = gson.fromJson(experimentForm, new TypeToken<List<Map<String, Object>>>() {
//        }.getType());
        List<Map<String,Object>> courseExpInfoList = (List<Map<String,Object>>) reqObject.get("experimentForm");
        int attendanceWeight =  (int)Math.round((Double)reqObject.get("attendanceWeight"));
        int practiceWeight = (int)Math.round((Double)reqObject.get("practiceWeight"));

        String courseID = instructorService.createCourse(title,instructorNumber,startDate,endDate);
        int result = 1;
        Map<String, Integer> map = new HashMap<>();
        if("".equals(courseID)){
            result = 0;
        }

        instructorService.addCourseExp(courseID,courseExpInfoList,attendanceWeight,practiceWeight);

        map.put("result",result);
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("registerGrade")
    public ApiResult registerGrade(@RequestBody JSONObject reqObject) {
        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String studentNumber = (String) reqObject.get("studentNumber");
        String expname = (String) reqObject.get("expname");
        double score = (double) reqObject.get("score");
        String comment  = (String) reqObject.get("comment");

        int result = 0;
        if(instructorService.registerGrade(courseID,classID,studentNumber,expname,score,comment)==1){
            result = 1;
        }

        Map<String,Integer> map = new HashMap<>();
        map.put("result",result);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("viewReflection")
    public ApiResult viewReflection(@RequestBody JSONObject reqObject) {
        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");

        List<Reflection> reflectionList = instructorService.getAllReflection(courseID,classID);

        List<Map<String, Object>> reflectionInfoList = new ArrayList<>();

        if(reflectionList != null){
            for(Reflection reflection : reflectionList){
                Student student = administratorService.getStudentByStudentNumber(reflection.getStudentNumber());
                Map<String, Object> map = new HashMap<>();
                map.put("studentNumber",student.getStudentNumber());
                map.put("studentName",student.getName());
                map.put("content",reflection.getContent());
                map.put("date",reflection.getDate());

                reflectionInfoList.add(map);
            }
        }

        return ApiResultHandler.buildApiResult(200,"",reflectionInfoList);
    }

    @PostMapping("viewPractice")
    public ApiResult viewPractice(@RequestBody JSONObject reqObject) {
        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");

        List<Practice> practiceList = instructorService.getPracticeListOfSection(courseID,classID);

        List<Map<String,Object>> practiceInfoList = new ArrayList<>();

        if(practiceList != null){
            for(Practice practice: practiceList){
                Map<String, Object> map = new HashMap<>();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                map.put("practiceName",practice.getPracticeName());
                map.put("startTime",df.format(practice.getStartTime()));
                map.put("endTime",df.format(practice.getEndTime()));
                map.put("practiceId",practice.getPracticeId());
                //当前时间
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(practice.getEndTime())){
                    map.put("status","正在进行");
                }else if(now.after(practice.getEndTime())){
                    map.put("status","已结束");
                }else{
                    map.put("status","尚未开始");
                }
                practiceInfoList.add(map);
            }
        }

        return ApiResultHandler.buildApiResult(200,"",practiceInfoList);
    }

    @PostMapping("publishPractice")
    public ApiResult publishPractice(@RequestBody JSONObject jsonObject) {
        String practiceName = jsonObject.getString("practiceName");
        String courseID = jsonObject.getString("courseID");
        String classID = jsonObject.getString("classID");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        Practice practice = new Practice();
        practice.setPracticeName(practiceName);
        practice.setClassId(classID);
        practice.setCourseId(courseID);
        practice.setStartTime(Timestamp.valueOf(startTime));
        practice.setEndTime(Timestamp.valueOf(endTime));
        practice.setChoiceId("1,2,3,4,5");
        practiceService.add(practice);
        return ApiResultHandler.success(null);
    }


    @PostMapping("addQuestion")
    public ApiResult addQuestion(@RequestBody JSONObject reqObject){

        String choiceQuestion = (String) reqObject.get("choiceQuestion");
        String choiceOption = (String) reqObject.get("choiceOption");
        int choiceDifficulty = (int) reqObject.get("choiceDifficulty");
        String choiceAnswer = (String) reqObject.get("choiceAnswer");
        String choiceAnalysis = (String) reqObject.get("choiceAnalysis");
        double choiceScore = (Double) reqObject.get("choiceScore");

        choiceQuestionRepository.save(ChoiceQuestion.builder()
                .choiceQuestion(choiceQuestion)
                .choiceOption(choiceOption)
                .choiceDifficulty(choiceDifficulty)
                .choiceAnswer(choiceAnswer)
                .choiceAnalysis(choiceAnalysis)
                .choiceScore(choiceScore)
                .build());
        return ApiResultHandler.success("添加成功");
    }

    @PostMapping("")
    public ApiResult getQuestion(@RequestBody JSONObject reqObject) {

        List<ChoiceQuestion> choiceQuestionList = choiceQuestionRepository.findAll();

        return ApiResultHandler.buildApiResult(200,"",choiceQuestionList);
    }

    @PostMapping("shutDownCourse")
    public ApiResult shutDownCourse(@RequestBody JSONObject reqObject) {

        String courseID = (String) reqObject.get("courseID");

        //关闭课程
        int result= courseRepository.UpdateFlagOfCourseByCourseID(courseID,-1);
        return ApiResultHandler.success("课程已关闭");
    }

}
