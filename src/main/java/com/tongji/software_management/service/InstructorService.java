package com.tongji.software_management.service;

import com.tongji.software_management.dao.*;
import com.tongji.software_management.entity.DBEntity.*;
import com.tongji.software_management.utils.RandomHandler;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
@Service
public class InstructorService {
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
    private ReflectionRepository reflectionRepository;
    @Resource
    private TeachesRepository teachesRepository;
    @Resource
    private CourseRepository courseRepository;
    @Resource
    private ExpReportRepository expReportRepository;
    @Resource
    private SectionRepository sectionRepository;
    @Resource
    private InstructorRepository instructorRepository;
    @Resource
    private ReferenceRepository referenceRepository;
    @Resource
    private ChoiceQuestionRepository choiceQuestionRepository;
    @Resource
    private CounterRepository counterRepository;

    public Section getSection(String courseID, String classID) {
        return sectionRepository.findSectionByCourseIdAndClassId(courseID, classID);
    }

    public List<Map<String, String>> GetSections(String instructorNumber) {
        //创建存储sections信息的列表
        List<Map<String, String>> sectionInfoList = new ArrayList<>();
        List<Teaches> teachesList = teachesRepository.findTeachesByInstructorNumber(instructorNumber);

        for (Teaches t : teachesList) {
            Map<String, String> sectionInfo = new HashMap<>();
            //获取courseID和classID
            sectionInfo.put("courseID", t.getCourseId());
            sectionInfo.put("classID", t.getClassId());
            //获取课程名
            Course course = courseRepository.findCourseByCourseId(t.getCourseId());
            if(course.getFlag()==-1){
                //课程无效
                continue;
            }
            sectionInfo.put("title", course.getTitle());
            //获取duty信息
            String duty = "教师";
            if (course.getInstructorNumber().equals(instructorNumber)) {
                duty = duty + ",责任教师";
            }
            sectionInfo.put("duty", duty);

            sectionInfoList.add(sectionInfo);
        }

        return sectionInfoList;
    }

    public List<Map<String, String>> GetCourseExpInfo(String courseID,String classID) {
        List<Map<String, String>> courseExpInfoList = new ArrayList<>();

        List<CourseExp> courseExpList = courseExpRepository.findCourseExpsByCourseId(courseID);
        //开始整理信息
        for (CourseExp c : courseExpList) {
            if("考勤".equals(c.getExpname()) || "对抗练习".equals(c.getExpname())){
                continue;
            }
            Map<String, String> courseExpInfo = new HashMap<>();
            courseExpInfo.put("title", c.getExpname());
            courseExpInfo.put("priority", Integer.toString(c.getPriority()));
            courseExpInfo.put("difficulty", Integer.toString(c.getDifficulty()));
            courseExpInfo.put("weight", c.getPercent() + "%");
            String startDate = "";
            String status = "未发布";
            Experiment experiment = experimentRepository.findExperimentByCourseIdAndClassIdAndExpname(courseID,classID,c.getExpname());
            if(experiment!=null){
                startDate = experiment.getStartDate();
                status = "已发布";
            }
            courseExpInfo.put("startDate",startDate);
            courseExpInfo.put("status", status);
            //加入到信息列表
            courseExpInfoList.add(courseExpInfo);
        }

        return courseExpInfoList;
    }

    public int ReleaseExperiment(String courseID, String expname, String classID,String startDate,String endDate, String expInfo) {
        experimentRepository.save(Experiment.builder()
                .courseId(courseID)
                .expname(expname)
                .classId(classID)
                .startDate(startDate)
                .endDate(endDate)
                .expInfo(expInfo)
                .build());
        return 1;
    }

    public Map<String, String> ExamineExperimentInfo(String courseID, String classID, String expname) {
        Map<String, String> experimentInfo = new HashMap<>();
        //根据主码锁定实验
        Experiment experiment = experimentRepository.findExperimentByCourseIdAndClassIdAndExpname(courseID, classID, expname);
        experimentInfo.put("startDate", experiment.getStartDate());
        experimentInfo.put("endDate", experiment.getEndDate());
        experimentInfo.put("expInfo", experiment.getExpInfo());

        return experimentInfo;
    }

    public int ModifyExperiment(String courseID, String classID, String expname, String endDate, String expInfo) {
        experimentRepository.save(Experiment.builder()
                .courseId(courseID)
                .classId(classID)
                .expname(expname)
                .endDate(endDate)
                .expInfo(expInfo)
                .build());
        return 1;
    }

    public int ReleaseNotice(String courseID, String classID, String instructorNumber, String content, String date, String title) {
        noticeRepository.save(Notice.builder()
                .courseId(courseID)
                .classId(classID)
                .instructorNumber(instructorNumber)
                .content(content)
                .date(date)
                .title(title)
                .build());
        return 1;
    }

    public int DeleteNotice(String courseID, String classID, String instructorNumber, String date) {
        return noticeRepository.deleteNoticeByCourseIdAndClassIdAndInstructorNumberAndDate(courseID, classID, instructorNumber, date);
    }

    public int ReleaseReportDesc(String courseID, String classID, String expname, String reportName, String reportInfo, String startDate, String endDate, String fileType) {
        expReportRepository.save(ExpReport.builder()
                .courseId(courseID)
                .classId(classID)
                .expname(expname)
                .reportInfo(reportInfo)
                .reportInfo(reportInfo)
                .startDate(startDate)
                .endDate(endDate)
                .fileType(fileType)
                .build());
        return 1;
    }

    public int DeleteReportDesc(String courseID, String classID, String expname, String reportName) {
        return expReportRepository.deleteExpReportByCourseIdAndClassIdAndExpnameAndReportName(courseID, classID, expname, reportName);
    }

    public int ModifyReportDesc(String courseID, String classID, String expname, String reportName, String reportInfo, String endDate, String fileType) {
        return expReportRepository.UpdateReportDesc(courseID, classID, expname, reportName, reportInfo, endDate, fileType);
    }

    public List<Map<String, String>> GetSectionInfoOfCourse(String courseID) {
        List<Map<String, String>> sectionInfoList = new ArrayList<>();
        //先找到课程的所有班级（有些班级可能暂时没有老师教授，也可能有多个教师教授）
        List<Section> sectionList = sectionRepository.findSectionByCourseId(courseID);
        for (Section section : sectionList) {
            Map<String, String> sectionInfo = new HashMap<>();
            //classID,day,time,currentNumber,maxNubmer
            sectionInfo.put("classID", section.getClassId());
            sectionInfo.put("day", "星期" + section.getDay());
            sectionInfo.put("time", "第" + section.getTime() + "节课");
            sectionInfo.put("currentNumber", section.getCurrentNumber() + "人");
            sectionInfo.put("maxNumber", section.getNumber() + "人");
            //开始从teaches中找任课教师
            List<Teaches> instructorList = teachesRepository.findTeachesByCourseIdAndClassId(courseID, section.getClassId());
            if (instructorList == null) {
                //没有任课教师
                sectionInfo.put("instructorName", "暂无");
                sectionInfo.put("instructorNumber", "暂无");
            } else {
                //有任课教师
                StringBuilder instructorName = new StringBuilder();
                StringBuilder instructorNumber = new StringBuilder();
                Iterator<Teaches> iterator = instructorList.iterator();
                while (iterator.hasNext()) {
                    Teaches t = iterator.next();
                    //找教师名
                    Instructor instructor = instructorRepository.findInstructorByInstructorNumber(t.getInstructorNumber());
                    instructorName.append(instructor.getName());
                    instructorNumber.append(instructor.getInstructorNumber());
                    if (iterator.hasNext()) {
                        instructorName.append(",");
                        instructorNumber.append(",");
                    }
                }
                sectionInfo.put("instructorName", instructorName.toString());
                sectionInfo.put("instructorNumber", instructorNumber.toString());
            }
            sectionInfoList.add(sectionInfo);
        }

        return sectionInfoList;
    }

    public List<ExpScore> getSubmittedStudentList(String courseID, String classID, String expname) {
        return expScoreRepository.findExpScoresByCourseIdAndExpnameAndClassId(courseID,expname,classID);
    }

    public List<String> getStudentNumbersByCourseIDAndClassID(String courseID, String classID) {
        List<Takes> takesList = takesRepository.findTakesByCourseIdAndClassId(courseID,classID);
        List<String> studentList = new ArrayList<>();
        for(Takes takes : takesList){
            studentList.add(takes.getStudentNumber());
        }
        return studentList;
    }

    public boolean checkReference(String fileUrl) {
        return referenceRepository.findReferenceByFileUrl(fileUrl) != null;
    }

    public int recordCommit(String courseID, String classID, String instructorNumber, String fileUrl) {
        referenceRepository.save(Reference.builder()
                .courseId(courseID)
                .classId(classID)
                .instructorNumber(instructorNumber)
                .fileUrl(fileUrl)
                .build());
        return 1;
    }

    public int getCourseAttendPercent(String courseID) {
        CourseExp courseExp = courseExpRepository.findCourseExpByCourseIdAndExpname(courseID,"考勤");
        return courseExp.getPercent();
    }

    public List<Attend> getAttendsBefore(String courseID, String classID) {
        return attendRepository.findAttendsByCourseIdAndClassId(courseID,classID);
    }

    public int addAttend(String courseID, String classID, String attendName, String startTime, String endTime) {
        attendRepository.save(Attend.builder()
                .courseId(courseID)
                .classId(classID)
                .title(attendName)
                .startTime(startTime)
                .endTime(endTime)
                .build());
        return 1;
    }

    public int deleteReference(String fileUrl) {
        return referenceRepository.deleteReferenceByFileUrl(fileUrl);
    }

    public int addSection(String courseID,String instructorNumber, int day, int time, int number) {
        Counter counter = counterRepository.findCounterById(1);
        //上一个classID
        int classID = counter.getClassId();
        //本课程的classID
        String newClassID = classID + 1 + "";
        sectionRepository.save(Section.builder().courseId(courseID).classId(newClassID).day(day).time(time).number(number).build());

        //更新courseID计数
        counterRepository.UpdateCourseIDOfCounter(classID+1);
        teachesRepository.save(Teaches.builder()
                .instructorNumber(instructorNumber)
                .courseId(courseID)
                .classId(newClassID)
                .build());
        return 1;
    }

    public String createCourse(String title, String instructorNumber, String startDate, String endDate) {
        Counter counter = counterRepository.findCounterById(1);
        //上一个courseID
        int courseId = counter.getCourseId();
        //本课程的courseID
        String newCourseID = courseId + 1 + "";
        //添加课程
        courseRepository.save(Course.builder().courseId(newCourseID).title(title).instructorNumber(instructorNumber).startDate(startDate).endDate(endDate).build());

        //更新courseID计数
        counterRepository.UpdateCourseIDOfCounter(courseId+1);
        return newCourseID;
    }

    public void addCourseExp(String courseID, List<Map<String,Object>> courseExpInfoList,int attendanceWeight,int practiceWeight) {
        //添加实验
        for(Map<String, Object> c : courseExpInfoList){
            courseExpRepository.save(CourseExp.builder()
                    .courseId(courseID)
                    .expname((String)c.get("title"))
                    .percent((int)Math.round((Double)c.get("weight")))
                    .priority((int)Math.round((Double)c.get("priority")))
                    .difficulty((int)Math.round((Double)c.get("difficulty")))
                    .build());
        }
        //添加考勤
        courseExpRepository.save(CourseExp.builder().courseId(courseID).expname("考勤").percent(attendanceWeight).priority(1).difficulty(1).build());
        //添加对抗练习
        courseExpRepository.save(CourseExp.builder().courseId(courseID).expname("对抗练习").percent(attendanceWeight).priority(1).difficulty(1).build());
    }

    public int registerGrade(String courseID, String classID, String studentNumber, String expname, double score, String comment) {
        return expScoreRepository.UpdateExpScore(studentNumber,courseID,expname,classID,score,comment);
    }

    public List<ChoiceQuestion> getRandomQuestionList(int size) {
        //随机数的上限
        int count = (int) choiceQuestionRepository.count();
        List<ChoiceQuestion> choiceQuestionList = new ArrayList<ChoiceQuestion>();
        HashSet<Integer> set = RandomHandler.createNonRepeatingRandom(size, 1, count);
        for (Integer questionId : set) {
            choiceQuestionList.add(choiceQuestionRepository.findChoiceQuestionByChoiceId(questionId));
        }
        return choiceQuestionList;
    }

    public List<AttendScore> getAttendScoreByCourseIDAndClassIDAndTitle(String courseID, String classID, String title) {
        return attendScoreRepository.findAttendScoreByCourseIdAndClassIdAndTitle(courseID, classID, title);
    }

    public Student getStudentByStudentNumber(String studentNumber) {
        return studentRepository.findStudentByStudentNumber(studentNumber);
    }

//    public List<Takes> getTakesNotInAttendScore(String courseID, String classID, String title) {
//        return takesRepository.findTakesNotInAttendScore(courseID, classID, title);
//    }

    public List<Teaches> getTeachesListByInstructorNumber(String instructorNumber) {
        return teachesRepository.findTeachesByInstructorNumber(instructorNumber);
    }

    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    public List<Reflection> getAllReflection(String courseID, String classID) {
        return reflectionRepository.findReflectionListByCourseIdAndClassId(courseID,classID);
    }

    public List<Practice> getPracticeListOfSection(String courseID, String classID) {
        return practiceRepository.findPracticesByCourseIdAndClassId(courseID,classID);
    }
}
