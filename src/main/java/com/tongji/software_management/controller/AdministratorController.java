package com.tongji.software_management.controller;

import com.alibaba.fastjson.JSONObject;
import com.tongji.software_management.entity.DBEntity.Administrator;
import com.tongji.software_management.entity.DBEntity.Course;
import com.tongji.software_management.entity.DBEntity.Instructor;
import com.tongji.software_management.entity.DBEntity.Student;
import com.tongji.software_management.entity.LogicalEntity.ApiResult;
import com.tongji.software_management.entity.LogicalEntity.UserInfo;
import com.tongji.software_management.service.AdministratorService;
import com.tongji.software_management.service.InstructorService;
import com.tongji.software_management.service.StudentService;
import com.tongji.software_management.service.UserService;
import com.tongji.software_management.utils.ApiResultHandler;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdministratorController {

    @Resource
    private AdministratorService administrationService;
    @Resource
    protected UserService userService;
    @Resource
    private StudentService studentService;
    @Resource
    private InstructorService instructorService;

    @PostMapping("/alterUserInformation")
    public ApiResult alterUserInformation(@RequestBody JSONObject reqObject) {

        String userNumber = (String) reqObject.get("userNumber");
        //主码
        String email = (String) reqObject.get("email");
        String name = (String) reqObject.get("name");
        int sex = (reqObject.get("sex").equals("男")) ? 1 : 0;
        String phoneNumber = (String) reqObject.get("phoneNumber");
        String identify = (String) reqObject.get("identify");
        int msg = 0;
        if ("student".equals(identify)) {
            msg = userService.alterStudentInformation(userNumber, email, name, sex, phoneNumber);
        } else if ("teacher".equals(identify)) {
            msg = userService.alterInstructorInformation(userNumber, email, name, sex, phoneNumber);
        }

        //返回响应
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("/getAdministrationInfo")
    public ApiResult getAdministrationInfo(@RequestBody JSONObject reqObject) {

        String adminNumber = (String) reqObject.get("adminNumber");
        Administrator administrator = administrationService.getAdministrationInfo(adminNumber);

        //返回响应
        Map<String, Object> map = new HashMap<>();
        map.put("admin", administrator);
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("/getStudentByStudentNumber")
    public ApiResult getStudentByStudentNumber(@RequestBody JSONObject reqObject) {

        String studentNumber = (String) reqObject.get("studentNumber");
        Student student = administrationService.getStudentByStudentNumber(studentNumber);

        //返回响应
        Map<String, Object> map = new HashMap<>();
        map.put("student", student);
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    //创建新的学生账号 √
    @PostMapping("/createStudent")
    public ApiResult createStudent(@RequestBody JSONObject reqObject) {

        String name = (String) reqObject.get("name");
        String sexString = (String) reqObject.get("sex");
        int sex = 0;
        if (sexString.equals("男")) {
            sex = 1;
        }
        String studentNumber = (String) reqObject.get("studentNumber");
        String email = (String) reqObject.get("email");
        String phoneNumber = (String) reqObject.get("phoneNumber");

        String msg;        //记录添加结果:成功”success“、失败的话msg包含错误提示
        msg = administrationService.AddStudent(studentNumber, email, name, phoneNumber, sex);
        //返回响应
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    //创建教师账号 √
    @PostMapping("/createTeacher")
    public ApiResult createTeacher(@RequestBody JSONObject reqObject) {

        String name = (String) reqObject.get("name");
        String sexString = (String) reqObject.get("sex");
        int sex = 0;
        if (sexString.equals("男")) {
            sex = 1;
        }
        String instructorNumber = (String) reqObject.get("instructorNumber");
        String email = (String) reqObject.get("email");
        String phoneNumber = (String) reqObject.get("phoneNumber");

        String msg;        //记录添加结果:成功”success“、失败的话msg包含错误提示
        msg = administrationService.AddInstructor(instructorNumber, email, name, phoneNumber, sex);
        //返回响应
        Map<String, Object> map = new HashMap<>();
        map.put("result", msg);
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    //根据学生的学号获得他参与的课程信息 √
    @PostMapping("/getTakesByStudentNumber")
    public ApiResult getTakesByStudentNumber(@RequestBody JSONObject reqObject) {

        String studentNumber = (String) reqObject.get("studentNumber");
        //获取查询结果
        Map<String, Object> map = administrationService.GetTakesInfoByStudentNumber(studentNumber);
        //返回响应
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    //根据老师的工号获得老师信息，用于搜索对应的老师 √
    @PostMapping("/getTeacherByTeacherNumber")
    public ApiResult getTeacherByTeacherNumber(@RequestBody JSONObject reqObject) {

        String instructorNumber = (String) reqObject.get("instructorNumber");
        //将信息填入map
        Map<String, Object> map = administrationService.SearchInstructorByInstructorNumber(instructorNumber);
        //返回响应
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    //根据教师工号获得教授的课程信息 √
    @PostMapping("/getTeachesByTeacherNumber")
    public ApiResult getTeachesByTeacherNumber(@RequestBody JSONObject reqObject) {

        String instructorNumber = (String) reqObject.get("instructorNumber");
        //获取任课信息
        Map<String, Object> map = administrationService.GetTeachesInfoByInstructorNumber(instructorNumber);
        //JSON化后传给前端
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("/changeStudentDuty")
    public ApiResult changeStudentDuty(@RequestBody JSONObject reqObject) {

        String studentNumber = (String) reqObject.get("studentNumber");
        String courseID = (String) reqObject.get("courseID");
        String classID = (String) reqObject.get("classID");
        String duty = (String) reqObject.get("duty");
        //获取修改结果
        String msg = administrationService.ChangeStudentDuty(studentNumber, courseID, classID, duty);
        Map<String, Object> map = new HashMap<>();
        map.put("result", msg);
        if ("学生".equals(duty)) {
            map.put("duty", "助教");
        } else {
            map.put("duty", "学生");
        }

        return ApiResultHandler.buildApiResult(200,"",map);
    }

    //修改某个课程下该教师的职务。（将普通老师改为责任教师）
    @PostMapping("/changeDutyInstructor")
    public ApiResult changeDutyInstructor(@RequestBody JSONObject reqObject) {

        //获取 哪个老师 想设置为 哪个课程 的责任教师
        String instructorNumber = (String) reqObject.get("instructorNumber");
        String courseID = (String) reqObject.get("courseID");
        String duty = (String) reqObject.get("duty");
        //获取修改结果
        String msg = administrationService.ChangeDutyInstructor(instructorNumber, courseID);
        Map<String, Object> map = new HashMap<>();
        map.put("result", msg);
        if ("教师".equals(duty)) {
            map.put("duty", "责任教师");
        } else {
            map.put("duty", "教师");
        }
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    //查看某门课程下的责任教师
    @PostMapping("/checkTeacherDuty")
    public ApiResult checkTeacherDuty(@RequestBody JSONObject reqObject) {
        String courseID = (String) reqObject.get("courseID");
        //获取责任教师的工号和姓名
        Map<String, Object> map = administrationService.CheckTeacherDuty(courseID);
        return ApiResultHandler.buildApiResult(200,"",map);
    }

    @PostMapping("/DeleteUser")
    public ApiResult DeleteUser(@RequestBody JSONObject reqObject) {

        String email = (String) reqObject.get("email");
        //1.根据req中的具体请求，确定删除的是老师or学生
        String identity = (String) reqObject.get("identity");

        boolean result;
        if (identity.equals("student")) {
            result = administrationService.DeleteStudent(email);
        } else {
            result = administrationService.DeleteInstructor(email);
        }

        return ApiResultHandler.buildApiResult(200,"success",null);

    }

//    public ApiResult createStudentFromExcel(HttpServletRequest req, @RequestBody JSONObject jsonObject) {
//        System.out.println("先上传excel文件");
//        //后续需要读取的文件
//        File objectFile = null;
//        //1.先判断是否是多段数据,不是多段数据的话一定不是文件上传，后续操作无法展开
//        if (ServletFileUpload.isMultipartContent(req)) {
//            //2.创建FileItemFactory实现类
//            FileItemFactory fileItemFactory = new DiskFileItemFactory();
//            //3.用于解析上传数据的工具类ServletFileUpload
//            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
//            servletFileUpload.setHeaderEncoding("UTF-8"); //设置UTF-8编码
//            //4.解析数据，得到各个表单项FileItem
//            try {
//                List<FileItem> list = servletFileUpload.parseRequest(req);
//                String administratorNumber = null;
//                String filename = null;
//                FileItem fileItemToBeStore = null;
//                //5.判断每个表单项是否是文件
//                for (FileItem fileItem : list) {
//                    if (fileItem.isFormField()) {
//                        //获取存储位置信息
//                        String name = fileItem.getFieldName();   //普通表单项名字
//                        if (name.equals("administratorNumber")) {
//                            administratorNumber = fileItem.getString("UTF-8"); //参数UTF-8解决乱码问题
//                        }
//                    } else {
//                        //文件表单项
//                        filename = fileItem.getName();//上传的文件名
//                        System.out.println("文件：" + filename);
//                        fileItemToBeStore = fileItem; //先放着，等待文件路径确定后再写到磁盘
//                    }
//                }
//                //6.将文件写入磁盘
//                try {
//                    //使用Path类方便获取不同电脑下的当前文件所在的路径，随环境而变
//                    Path path = Paths.get(req.getServletContext().getRealPath(""));
//                    //定位到“整个项目”所在的路径
//                    path = path.getParent().getParent().getParent();
//                    //定位到文件应存放的路径
//                    Path fileDirectory = Paths.get(path.toString(), "web/WEB-INF/excels/" + administratorNumber);
//                    File file = new File(fileDirectory.toString());
//                    if (!file.isDirectory()) {
//                        file.mkdirs(); //这个方法可以将路径中确实的父类目录均创建出来
//                    }
//                    Path filePath = Paths.get(fileDirectory.toString(), filename);
//                    objectFile = new File(filePath.toString());
//                    fileItemToBeStore.write(objectFile);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } catch (FileUploadException e) {
//                e.printStackTrace();
//            }
//        }
//
//        //前端将本地文件拖拽到上传区，程序将文件拷贝一份存在web-inf路径下（此处应当调用上传文件的接口）
//        try {
//            // 创建输入流，读取Excel
//            InputStream is = new FileInputStream(objectFile.getAbsolutePath());
//            // jxl提供的Workbook类
//            Workbook wb = Workbook.getWorkbook(is);
//            // Excel的页签数量
//            int sheet_size = wb.getNumberOfSheets();
//            for (int index = 0; index < sheet_size; index++) {
//                // 每个页签创建一个Sheet对象
//                Sheet sheet = wb.getSheet(index);
//                // sheet.getRows()返回该页的总行数
//                for (int i = 1; i < sheet.getRows(); i++) {
//                    // sheet.getColumns()返回该页的总列数
//                    String studentNumber = sheet.getCell(0, i).getContents();
//                    String email = sheet.getCell(1, i).getContents();
//                    String name = sheet.getCell(2, i).getContents();
//                    int sex = Integer.parseInt(sheet.getCell(3, i).getContents());
//                    String phoneNumber = sheet.getCell(4, i).getContents();
//                    administrationService.AddStudent(studentNumber, email, name, phoneNumber, sex);
//
//                }
//            }
//        } catch (BiffException | IOException e) {
//            e.printStackTrace();
//        }
//    }

    //管理员查看需要审核的课程（责任教师申请的）
    @PostMapping("/getCourseApplied")
    public ApiResult getCourseApplied() {

        //等待审核的课程
        List<Course> courseAppliedList = administrationService.getCourseAppliedList();

        List<Map<String, String>> courseAppliedInfoList = new ArrayList<>();
        if (courseAppliedList != null) {
            for (Course course : courseAppliedList) {
                Instructor instructor = administrationService.getInstructorByInstructorNumber(course.getInstructorNumber());

                Map<String, String> map = new HashMap<>();

                map.put("title", course.getTitle());
                map.put("courseID", course.getCourseId());
                map.put("instructorNumber", course.getInstructorNumber());
                map.put("instructorName", instructor.getName());

                courseAppliedInfoList.add(map);
            }
        }

        return ApiResultHandler.buildApiResult(200,"",courseAppliedInfoList);
    }

    //审核课程
    @PostMapping("/AuditCourse")
    public ApiResult AuditCourse(@RequestBody JSONObject reqObject) {


        String courseID = (String) reqObject.get("courseID");
        //评审结果
        String result = (String) reqObject.get("result");

        administrationService.aduitCourse(courseID, result);
        return ApiResultHandler.success("");
    }

    //获取所有学生信息
    @PostMapping("getStudentInfo")
    public ApiResult getStudentInfo(@RequestBody JSONObject reqObject) {

        List<Student> studentList = studentService.getAllStudents();
        List<UserInfo> userInfoList = new ArrayList<>();
        if(studentList != null){
            for(Student student : studentList){
                UserInfo userInfo = new UserInfo();
                String sex = (student.getSex()==1)?"男":"女";
                userInfo.setStudentInfo(student.getName(),sex,student.getStudentNumber(),student.getPhoneNumber(),student.getEmail());
                userInfoList.add(userInfo);
            }
        }

        return ApiResultHandler.buildApiResult(200,"",userInfoList);
    }

    //获取所有老师信息
    @PostMapping("getTeacherInfo")
    public ApiResult getTeacherInfo(@RequestBody JSONObject reqObject) {

        List<Instructor> instructorList = instructorService.getAllInstructors();
        List<UserInfo> userInfoList = new ArrayList<>();
        if(instructorList != null){
            for(Instructor instructor : instructorList){
                UserInfo userInfo = new UserInfo();
                String sex = (instructor.getSex()==1)?"男":"女";
                userInfo.setTeacherInfo(instructor.getName(),sex,instructor.getInstructorNumber(),instructor.getPhoneNumber(),instructor.getEmail());
                userInfoList.add(userInfo);
            }
        }

        return ApiResultHandler.buildApiResult(200,"",userInfoList);
    }
}
