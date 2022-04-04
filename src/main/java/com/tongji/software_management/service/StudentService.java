package com.tongji.software_management.service;

import com.tongji.software_management.dao.*;
import com.tongji.software_management.entity.DBEntity.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class StudentService {
    @Resource
    private NoticeRepository noticeRepository;
    @Resource
    private ExpScoreRepository expScoreRepository;
    @Resource
    private TakesRepository takesRepository;
    @Resource
    private AttendScoreRepository attendScoreRepository;
    @Resource
    private CourseExpRepository courseExpRepository;
    @Resource
    private ExperimentRepository experimentRepository;
    @Resource
    private StudentRepository studentRepository;
    @Resource
    private AttendRepository attendRepository;
    @Resource
    private PracticeRepository practiceRepository;
    @Resource
    private PracticeScoreRepository practiceScoreRepository;
    @Resource
    private ReflectionRepository reflectionRepository;

    public List<Notice> getCourseNotice(String courseID, String classID) {
        //获取所有的通知
        return noticeRepository.findNoticesByCourseIdAndClassId(courseID,classID);
    }

    public ExpScore getExpScore(String courseID, String classID, String expname, String studentNumber) {
        String score = null;
        //查找数据库中该学生提交的实验对应的成绩信息。
        return expScoreRepository.findExpScoreByCourseIdAndClassIdAndExpnameAndStudentNumber(courseID, classID, expname, studentNumber);
    }

    public int recordCommit(String courseID, String classID, String expname, String studentNumber,String fileUrl) {
        //首次提交，则“添加”提交记录
        if(expScoreRepository.findExpScoreByCourseIdAndClassIdAndExpnameAndStudentNumber(courseID, classID, expname, studentNumber)==null){
            expScoreRepository.save(ExpScore.builder()
                    .courseId(courseID)
                    .classId(classID)
                    .expname(expname)
                    .studentNumber(studentNumber)
                    .build());
            return 1; //每次插入一行，这里应该是返回1吧
        }
        //后续重交，则覆盖文件信息
        return expScoreRepository.UpdateFileUrl(courseID, classID, expname, studentNumber, fileUrl);
    }

    public String getDuty(String courseID, String classID, String studentNumber) {
        Takes takes = takesRepository.findTakesByCourseIdAndClassIdAndStudentNumber(courseID,classID,studentNumber);
        return (takes.getStatus()==0)?"学生":"助教";
    }

    public int addAttendScore(String courseID, String classID, String title, String studentNumber, int onTime) {
        attendScoreRepository.save(AttendScore.builder()
                .courseId(courseID)
                .classId(classID)
                .title(title)
                .studentNumber(studentNumber)
                .onTime(onTime)
                .build());
        return 1; //每次插入一行，这里应该是返回1吧
    }

    public int deleteCommit(String fileUrl) {
        return expScoreRepository.deleteExpScoreByFileUrl(fileUrl);
    }

    public List<CourseExp> getCoursesByCourseID(String courseID) {
        return courseExpRepository.findCourseExpsByCourseId(courseID);
    }

    public List<Experiment> getExperimentByCourseIDAndClassID(String courseID, String classID) {
        return experimentRepository.findExperimentsByCourseIdAndClassId(courseID, classID);
    }

    public CourseExp getCourseExpByCourseIDAndExpname(String courseID, String expname) {
        return courseExpRepository.findCourseExpByCourseIdAndExpname(courseID, expname);
    }

    public List<Takes> getTakesListByStudentNumber(String studentNumber) {
        return takesRepository.findTakesByStudentNumber(studentNumber);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Experiment> getExperimentListByCourseIDAndClassID(String courseID, String classID) {
        return experimentRepository.findExperimentsByCourseIdAndClassId(courseID,classID);
    }

    public Map<String, Object> getGradeAndRankingOfExperiment(Experiment experiment, String studentNumber) {
        //按照score倒叙排列的名单
        List<ExpScore> expScoreList = expScoreRepository.findExpScoresByCourseIdAndClassIdAndExpnameDESC(experiment.getCourseId(),experiment.getClassId(),experiment.getExpname());
        Map<String,Object> map = new HashMap<>();
        map.put("name",experiment.getExpname());
        if(expScoreList == null){
            //还没有人提交报告，成绩为零分，默认第一名
            map.put("grade",0.0);
            map.put("ranking",1);
        }else{
            boolean exist = false;
            for(int i =0 ;i < expScoreList.size();i++){
                ExpScore expScore = expScoreList.get(i);
                //找到学生在排名中的位置
                if(expScore.getStudentNumber().equals(studentNumber)){
                    double score = expScore.getScore();
                    map.put("grade",(score==-1)?0:score);
                    map.put("ranking",i+1);
                    exist = true;
                    break;
                }
            }
            //没找到学生时，说明学生没有提交作业
            if(!exist) {
                map.put("grade", 0.0);
                map.put("ranking", expScoreList.size() + 1);
            }
        }
        return map;
    }

    public float getGradeOfAttendance(String courseID, String classID, String studentNumber) {
        //该课的各项成绩占比分配情况
        List<CourseExp> courseExpList = courseExpRepository.findCourseExpsByCourseId(courseID);
        //考勤成绩占比
        int percent = 0;
        for(CourseExp courseExp : courseExpList){
            if("考勤".equals(courseExp.getExpname())){
                percent = courseExp.getPercent();
            }
        }
        //该班发布的所有考勤
        List<Attend> attendList = attendRepository.findAttendsByCourseIdAndClassId(courseID,classID);
        //考勤总次数
        float totalCount = attendList.size();
        if(totalCount == 0){
            //老师如果从来没有发布过考勤，则默认考勤满分
            return percent;
        }
        //学生参加的考勤数
        float attendCount = 0;
        for(Attend attend : attendList){
            AttendScore attendScore = attendScoreRepository.findAttendScoreByCourseIdAndClassIdAndTitleAndStudentNumber(courseID, classID, attend.getTitle(), studentNumber);
            if(attendScore != null){
                //该次签到准时参加了，计入分数
                attendCount += 1;
            }
        }
        //返回考勤分数
        return attendCount/totalCount*percent;
    }

    public float getGradeOfExperiment(String courseID, String classID, String studentNumber) {
        //该课的各项成绩占比分配情况
        List<CourseExp> courseExpList = courseExpRepository.findCourseExpsByCourseId(courseID);
        //开始算实验成绩
        float expGrade = 0;
        for(CourseExp courseExp : courseExpList){
            //该实验的百分比
            float expPercent = courseExp.getPercent();
            //靠学生在本实验的成绩
            ExpScore expScore = expScoreRepository.findExpScoreByCourseIdAndClassIdAndExpnameAndStudentNumber(courseID,classID,courseExp.getExpname(),studentNumber);
            if(expScore!=null && expScore.getScore() != -1){
                //只计算已经提交且批改过的报告
                expGrade += expScore.getScore()*expPercent/100;
            }
        }
        return expGrade;
    }

    public float getGradeOfPractice(String courseID, String classID, String studentNumber) {
        //该课的各项成绩占比分配情况
        List<CourseExp> courseExpList = courseExpRepository.findCourseExpsByCourseId(courseID);
        //抗练习成绩总占比
        float percent = 0;
        for(CourseExp courseExp : courseExpList){
            if("对抗练习".equals(courseExp.getExpname())){
                percent = courseExp.getPercent();
            }
        }
        //对抗练习总成绩
        float sumScore = 0;
        //该班级发布的所有对抗练习
        List<Practice> practiceList = practiceRepository.findPracticesByCourseIdAndClassId(courseID,classID);
        if(practiceList == null){
            //如果没有对抗练习，默认满分
            return percent;
        }
        //查找每一个对抗练习中在该学生的小组中的排名，确定成绩
        for(Practice practice : practiceList){
            //该学生在本次对抗练习中的成绩信息
            PracticeScore practiceScore = practiceScoreRepository.findPracticeScoreByCourseIdAndClassIdAndPracticeNameAndStudentNumber(courseID,classID,practice.getPracticeName(),studentNumber);
            if(practiceScore == null){
                //没有参加该次对抗练习，成绩为0
                continue;
            }
            //排序后的小组成员
            List<PracticeScore> practiceScoreList = practiceScoreRepository.findPracticeScoreByGroup(courseID,classID,practice.getPracticeName(),practiceScore.getGroupNumber());
            //算成绩
            if(practiceScoreList != null){
                for(int i=0;i<practiceScoreList.size();i++){
                    if(practiceScoreList.get(i).getStudentNumber().equals(practiceScore.getStudentNumber())){
                        //找到所在的组的名次i(第一名100，第二名60，第三名20)
                        sumScore += ((3-i)*2-1)*20;
                        break;
                    }
                }
            }
        }

        return sumScore/practiceList.size()*percent/100;
    }

    public int writeReflection(String courseID, String classID, String studentNumber,String content,String date) {
        reflectionRepository.save(Reflection.builder()
                .courseId(courseID)
                .classId(classID)
                .studentNumber(studentNumber)
                .content(content)
                .date(date)
                .build());
        return 1;
    }
}
