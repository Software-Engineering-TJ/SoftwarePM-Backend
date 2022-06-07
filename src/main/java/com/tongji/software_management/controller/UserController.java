package com.tongji.software_management.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tongji.software_management.dao.CourseExpRepository;
import com.tongji.software_management.dao.CourseRepository;
import com.tongji.software_management.entity.DBEntity.*;
import com.tongji.software_management.entity.LogicalEntity.ApiResult;
import com.tongji.software_management.entity.LogicalEntity.Attendance;
import com.tongji.software_management.entity.LogicalEntity.ClassInfo;
import com.tongji.software_management.entity.LogicalEntity.User;
import com.tongji.software_management.service.AdministratorService;
import com.tongji.software_management.service.InstructorService;
import com.tongji.software_management.service.StudentService;
import com.tongji.software_management.service.UserService;
import com.tongji.software_management.utils.ApiResultHandler;
import com.tongji.software_management.utils.OSSUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private AdministratorService administrationService;
    @Resource
    private StudentService studentService;
    @Resource
    private InstructorService instructorService;
    @Resource
    private CourseRepository courseRepository;

    //发送邮件 √
    @PostMapping("sendEmail")
    public ApiResult sendEmail(HttpServletRequest req, @RequestBody JSONObject jsonObject) {

        int msg=-1;

        String userNumber = jsonObject.getString("userNumber");
        String isResetPassword = jsonObject.getString("isResetPassword");

        User user = userService.ifActivated(userNumber);
        int status;
        if (user == null) {
            msg = 0;//账号不存在
        } else {
            status = user.getStatus();

            //用户未激活 或 重置密码
            if (status == 0 || isResetPassword.equals("yes")) {
                if (user instanceof Student) {
                    user = (Student) user;
                } else if (user instanceof Instructor) {
                    user = (Instructor) user;
                }
                String desEmail = user.getEmail();
                // 创建Properties 类用于记录邮箱的一些属性
                Properties props = new Properties();
                // 表示SMTP发送邮件，必须进行身份验证
                props.put("mail.smtp.auth", "true");
                //此处填写SMTP服务器
                props.put("mail.smtp.host", "smtp.qq.com");
                //端口号，QQ邮箱端口587
                props.put("mail.smtp.port", "587");
                // 此处填写，写信人的账号
                props.put("mail.user", "939543598@qq.com");
                // 此处填写16位STMP口令
                props.put("mail.password", "dhexqmyoafxibbia");

                // 构建授权信息，用于进行SMTP进行身份验证
                Authenticator authenticator = new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        // 用户名、密码
                        String userName = props.getProperty("mail.user");
                        String password = props.getProperty("mail.password");
                        return new PasswordAuthentication(userName, password);
                    }
                };
                // 使用环境属性和授权信息，创建邮件会话
                Session mailSession = Session.getInstance(props, authenticator);
                // 创建邮件消息
                MimeMessage message = new MimeMessage(mailSession);
                // 设置发件人
                InternetAddress form = null;
                try {
                    form = new InternetAddress(props.getProperty("mail.user"));

                } catch (AddressException e) {
                    e.printStackTrace();
                }
                try {
                    message.setFrom(form);
                    // 设置收件人的邮箱
                    InternetAddress to = new InternetAddress(desEmail);
                    message.setRecipient(MimeMessage.RecipientType.TO, to);

                    // 设置邮件标题
                    message.setSubject("请验证您的身份");
                    //生成四位随机数
                    int verificationCode = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;
                    // 设置邮件的内容体
                    message.setContent("亲爱的" + user.getName() + "，你好！您的验证码是：" + verificationCode, "text/html;charset=UTF-8");

                    // 发送邮件
                    Transport.send(message);

                    //将发送给目标邮箱的验证码，返回给前端
                    HttpSession session = req.getSession();
                    session.setAttribute("verificationCode", Integer.toString(verificationCode));
                    // 发送成功
                    msg = 2;

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }else if (status == 1){
                msg=1;
            }

        }
        //返回响应
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("getUserStatus")
    public ApiResult getUserStatus(HttpServletRequest req, @RequestBody JSONObject jsonObject) {

        String userNumber = jsonObject.getString("userNumber");
        User user = userService.ifActivated(userNumber);
        String password;
        String status;
        int identify = 0;  //激活码

        if (user != null) {
            password = user.getPassword();
            status = Integer.toString(user.getStatus());
            //若未激活，返回激活码
            if (status.equals("0")) {
                identify = (int) req.getSession().getAttribute("verificationCode");
            }
        } else {
            //如果没有该用户，则返回如下内容
            userNumber = null;
            password = null;  // 表示账号信息不存在（那么也肯定未激活）
            status = null;
        }
        //需要返回的信息
        Map<String, Object> userInformation = new HashMap<>();
        userInformation.put("userNumber", userNumber);
        userInformation.put("password", password);
        userInformation.put("status", status);
        userInformation.put("identify", identify);
        return ApiResultHandler.buildApiResult(200,"",userInformation);
    }

//    public ApiResult login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        String reqJson = RequestJsonUtils.getJson(req);
//        Map<String, String> reqObject = gson.fromJson(reqJson, new TypeToken<Map<String, String>>() {
//        }.getType());
//
//        String userNumber = reqObject.get("userNumber");
//        //获取需要登录的用户对象，用于判断身份
//        User user = userService.ifActivated(userNumber);
//        //将userNumber信息加入到session,方便后续直接获取用户信息
//        HttpSession session = req.getSession();
//        session.setAttribute("userNumber", userNumber);
//        //转到登录成功后的界面
//        resp.addHeader("REDIRECT", "REDIRECT");//告诉ajax这是重定向
//        if (user instanceof Student) {
//            //标明身份
//            session.setAttribute("identity", "student");
//            //学生页面
//            resp.addHeader("CONTEXTPATH", "/SoftwareEngineering/pages/student/sIndex.html");//重定向地址
//        } else if (user instanceof Instructor) {
//            session.setAttribute("identity", "instructor");
//            //教师页面
//            resp.addHeader("CONTEXTPATH", "/SoftwareEngineering/pages/teacher/tIndex.html");//重定向地址
//        } else {
//            session.setAttribute("identity", "administrator");
//            //管理员页面
//            resp.addHeader("CONTEXTPATH", "/SoftwareEngineering/pages/administrator/aIndex.html");//重定向地址
//        }
//        //激活账户
//        if (reqObject.get("identify") != null && !reqObject.get("identify").equals("")) {
//            //如果首次激活，则将数据库用户激活状态设置为“1”
//            userService.activateAccount((String) session.getAttribute("identity"), user.getEmail());
//        }
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        resp.addHeader("access-control-expose-headers", "REDIRECT,CONTEXTPATH");
//    }

    @PostMapping("getUserInfo")
    public ApiResult getUserInfo(HttpServletRequest req, @RequestBody JSONObject jsonObject) {
        //先获取userNumber信息
        String userNumber = (String) req.getSession().getAttribute("userNumber");
        //从数据库获取用户信息
        User user = userService.ifActivated(userNumber);
        Map<String, String> map = new HashMap<>();
        map.put("userNumber", user.getUserNumber());
        map.put("name", user.getName());
        map.put("sex", (user.getSex() == 1) ? "男" : "女");
        map.put("email", user.getEmail());
        map.put("phoneNumber", user.getPhoneNumber());
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("changeUserInfo")
    public ApiResult changeUserInfo(HttpServletRequest req, @RequestBody JSONObject jsonObject) {

        String userNumber = jsonObject.getString("userNumber");
        String phoneNumber = jsonObject.getString("phoneNumber");
        String email = jsonObject.getString("email");

        //获取用户身份
        String identity = (String) req.getSession().getAttribute("identity");
        //修改结果
        int result = userService.alterUserInfo(identity, userNumber, phoneNumber, email);
        Map<String, Integer> map = new HashMap<>();
        map.put("result", result);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("verify")
    public ApiResult verify(HttpServletRequest req, @RequestBody JSONObject jsonObject) {

        //获取用户输入的验证码
        HttpSession session = req.getSession();
        String verificationCode = jsonObject.getString("verificationCode");
        //获取session中存储的正确验证码
        String correctCode =(String) session.getAttribute("verificationCode");
        //结果
        int result = 0;
        if (verificationCode.equals(correctCode)) {
            result = 1;
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("result", result);

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("changePassword")
    public ApiResult changePassword(HttpServletRequest req, @RequestBody JSONObject jsonObject) {

        String userNumber = jsonObject.getString("userNumber");
        String oldPassword = jsonObject.getString("oldPassword");
        String newPassword = jsonObject.getString("newPassword");
        //获取用户身份
        String identity = (String) req.getSession().getAttribute("identity");
        //修改结果
        int result = 0;
        if (oldPassword.equals(userService.getPassword(identity, userNumber))) {
            //旧密码输入正确，设置新密码
            result = userService.changePassword(identity, userNumber, newPassword);
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("result", result);
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("getReportDesc")
    public ApiResult getReportDesc(@RequestBody JSONObject jsonObject) {

        String courseID = jsonObject.getString("courseID");
        String classID = jsonObject.getString("classID");
        //获取该课程班级的所有实验描述列表
        List<Map<String, String>> expReportInfoList = userService.getExpReports(courseID, classID);

        return ApiResultHandler.buildApiResult(200,"",expReportInfoList);
    }

    @PostMapping("getExpReport")
    public ApiResult getExpReport(@RequestBody JSONObject jsonObject) {

        String courseID = jsonObject.getString("courseID");
        String classID = jsonObject.getString("classID");
        String expname = jsonObject.getString("expname");

        //获得所有已经提交的报告
        List<ExpScore> expScoreList = userService.getExpScoresOfExpname(courseID,classID,expname);

        List<Map<String,Object>> fileInfoList = new ArrayList<>();
        //开始完善需要返回的信息
        for(ExpScore expScore : expScoreList){
            //报告提交者
            String studentNumber = expScore.getStudentNumber();
            Student student = administrationService.getStudentByStudentNumber(studentNumber);
            //fileUrl
            String fileUrl = expScore.getFileUrl();
            //文件的元数据
            Map<String, String> fileInfo = OSSUtils.getObjectMeta(fileUrl);
            //统计前端需要的信息
            Map<String,Object> reportInfo = new HashMap<>();
            reportInfo.put("studentNumber",studentNumber);
            reportInfo.put("studentName",student.getName());
            reportInfo.put("filename",fileInfo.get("fileName"));
            reportInfo.put("fileSize",fileInfo.get("fileSize"));
            reportInfo.put("uploadTime",fileInfo.get("uploadDate"));
            reportInfo.put("url",fileUrl);

            fileInfoList.add(reportInfo);
        }
        return ApiResultHandler.buildApiResult(200,"",fileInfoList);
    }

    @PostMapping("getReferenceMaterial")
    public ApiResult getReferenceMaterial(@RequestBody JSONObject jsonObject) {

        String courseID = jsonObject.getString("courseID");
        String classID = jsonObject.getString("classID");

        List<Reference> referenceList = userService.getReferencesOfSection(courseID, classID);
        //开始完善返回信息
        List<Map<String, Object>> referenceInfoList = new ArrayList<>();
        for(Reference reference : referenceList){
            //文件上传者
            String  instructorNumber = reference.getInstructorNumber();
            Map<String, Object> instructorInfo = administrationService.SearchInstructorByInstructorNumber(instructorNumber);
            String upLoadUser = (String) instructorInfo.get("name");
            //fileUrl
            String fileUrl = reference.getFileUrl();
            //文件的元数据
            Map<String, String> fileInfo = OSSUtils.getObjectMeta(fileUrl);
            //统计前端需要的信息
            Map<String,Object> referenceInfo = new HashMap<>();
            referenceInfo.put("fileName",fileInfo.get("fileName"));
            referenceInfo.put("url",fileUrl);
            referenceInfo.put("fileSize",fileInfo.get("fileSize"));
            referenceInfo.put("upLoadDate",fileInfo.get("upLoadDate"));
            referenceInfo.put("upLoadUser",upLoadUser);

            referenceInfoList.add(referenceInfo);
        }
        return ApiResultHandler.buildApiResult(200,"",referenceInfoList);
    }

    @PostMapping("getClassInfo")
    public ApiResult getClassInfo(@RequestBody JSONObject jsonObject) {

        String courseID = jsonObject.getString("courseID");
        String classID = jsonObject.getString("classID");

        ClassInfo classInfo = userService.getClassInfo(courseID, classID);
       return ApiResultHandler.buildApiResult(200,"",classInfo);
    }

    @PostMapping("getAttendanceInfo")
    public ApiResult getAttendanceInfo(@RequestBody JSONObject jsonObject) {

        String courseID = jsonObject.getString("courseID");
        String classID = jsonObject.getString("classID");

        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(date);//获取当前时间

        List<Attend> attendList = userService.getAttendInfo(courseID, classID);
        List<Attendance> attendanceList = new ArrayList<Attendance>();
        for (Attend attend : attendList) {
            Attendance attendance = new Attendance();
            attendance.setAttendanceID("没有");
            attendance.setAttendanceName(attend.getTitle());
            attendance.setStartTime(attend.getStartTime());
            attendance.setEndTime(attend.getEndTime());
            System.out.println(currentTime);
            if (currentTime.compareTo(attend.getStartTime()) >= 0 && currentTime.compareTo(attend.getEndTime()) <= 0) {
                attendance.setStatus("正在进行");
            }
            else if (currentTime.compareTo(attend.getStartTime()) < 0) {
                attendance.setStatus("未开始");
            }
            else {
                attendance.setStatus("已结束");
            }
            attendanceList.add(attendance);
        }

        return ApiResultHandler.buildApiResult(200,"",attendanceList);
    }

    @PostMapping("getAttendanceInfoStu")
    public ApiResult getAttendanceInfoStu(@RequestBody JSONObject jsonObject) {

        String courseID = jsonObject.getString("courseID");
        String classID = jsonObject.getString("classID");
        String studentNumber = jsonObject.getString("studentNumber");

        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(date);//获取当前时间

        //获取所有签到
        List<Attend> attendList = userService.getAttendInfo(courseID, classID);
        List<Attendance> attendanceList = new ArrayList<Attendance>();
        for (Attend attend : attendList) {
            Attendance attendance = new Attendance();

            attendance.setAttendanceID("没有");
            String title = attend.getTitle();
            attendance.setAttendanceName(title);
            attendance.setStartTime(attend.getStartTime());
            attendance.setEndTime(attend.getEndTime());

            boolean flag = userService.judgeAttendScoreIfExist(courseID, classID, title, studentNumber);
            if (flag) {
                attendance.setStatus("已签到");
            }
            else {
                if (currentTime.compareTo(attend.getEndTime()) <= 0) {
                    attendance.setStatus("未签到");
                }
                else {
                    attendance.setStatus("已过期");
                }
            }

            attendanceList.add(attendance);
        }
        return ApiResultHandler.buildApiResult(200,"",attendanceList);
    }

    @PostMapping("getSchedule")
    public ApiResult getSchedule(HttpServletRequest req, @RequestBody JSONObject jsonObject) {
        HttpSession session = req.getSession();

        //日程信息
        List<Map<String,String>> scheduleList = new ArrayList<>();
        //课程列表
        List<?> sectionList = new ArrayList<>();
        //用户登录的身份
        String identity = (String) session.getAttribute("identity");
        if(identity == null){
            //用户没有登录
            return ApiResultHandler.fail("");
        }else{
            String userNumber = (String) session.getAttribute("userNumber");
            if ("student".equals(identity)) {
                //学生上的课
                sectionList = studentService.getTakesListByStudentNumber(userNumber);
            } else {
                //老师交的课
                sectionList = instructorService.getTeachesListByInstructorNumber(userNumber);
            }
            if (sectionList != null) {
                for (int i = 0; i < sectionList.size(); i++) {
                    String courseID = "";
                    String classID = "";
                    if ("student".equals(identity)) {
                        Takes takes = (Takes) sectionList.get(i);
                        courseID = takes.getCourseId();
                        classID = takes.getClassId();
                    } else {
                        Teaches teaches = (Teaches) sectionList.get(i);
                        courseID = teaches.getCourseId();
                        classID = teaches.getClassId();
                    }
                    Course course = courseRepository.findCourseByCourseId(courseID);
                    if(course.getFlag()==-1){
                        //课程已无效
                        continue;
                    }
                    //该课程下的试验任务
                    List<Experiment> experimentList = userService.getExperimentListByCourseIDAndClassID(courseID, classID);
                    if (experimentList != null) {
                        for (Experiment experiment : experimentList) {
                            Map<String, String> map = new HashMap<>();
                            map.put("title", experiment.getExpname());
                            map.put("start", experiment.getStartDate());
                            map.put("end", experiment.getEndDate());

                            scheduleList.add(map);
                        }
                    }
                    //该课程下的实验报告
                    List<ExpReport> expReportList = userService.getExpReportListByCourseIDAndClassID(courseID,classID);
                    if(expReportList != null){
                        for(ExpReport expReport : expReportList){
                            Map<String, String> map = new HashMap<>();
                            map.put("title", expReport.getReportName());
                            map.put("start", expReport.getStartDate());
                            map.put("end", expReport.getEndDate());

                            scheduleList.add(map);
                        }
                    }
                }
            }
        }
        return ApiResultHandler.buildApiResult(200,"",scheduleList);
    }



}
