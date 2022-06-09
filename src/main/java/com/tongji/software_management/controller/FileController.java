package com.tongji.software_management.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.google.gson.reflect.TypeToken;
import com.tongji.software_management.entity.LogicalEntity.ApiResult;
import com.tongji.software_management.service.InstructorService;
import com.tongji.software_management.service.StudentService;
import com.tongji.software_management.utils.ApiResultHandler;
import com.tongji.software_management.utils.OSSUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    /**
     * 文件上传oss（老师和学生均用此接口）
     */
    @PostMapping("upload")
    public ApiResult uploadFile(@RequestParam("courseID")String courseID,
                                @RequestParam("classID")String classID,
                                @RequestParam(value = "expname",required = false)String expname,
                                @RequestParam("userNumber")String userNumber,
                                @RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            return ApiResultHandler.fail("文件不能为空");
        }
        String filename = file.getOriginalFilename();

        //将文件写入oss
        if(expname == null){
            //本次是老师上传参考资料
            String filePath = courseID + "/" + classID + "/" + userNumber + "/" + filename;
            String fileUrl = OSSUtils.uploadFile(filePath, file);
            //记录老师提交的资料信息：考虑重复提交同名文件（不用再次纪录）和提交新文件（插入一条记录）
            if(!instructorService.checkReference(fileUrl)){
                instructorService.recordCommit(courseID,classID,userNumber,fileUrl);
            }
        }else{
            //本次是学生上传实验报告
            String filePath = courseID + "/" + classID + "/" + expname + "/" + userNumber + "/" + filename;
            String fileUrl = OSSUtils.uploadFile(filePath, file);
            //将实验提交记录更新到数据库
            studentService.recordCommit(courseID,classID,expname,userNumber,fileUrl);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("result",1);
        return ApiResultHandler.success(map);
    }

    /**
     * 下载oss文件
     */
    @GetMapping("download")
    public void downloadFile(HttpServletResponse resp,
                             @RequestParam("fileUrl")String fileUrl)throws Exception{
        System.out.println("开始下载oss文件");

        //获取文件名
        List<String> stringList = List.of(fileUrl.split("/"));
        String filename = stringList.get(stringList.size()-1);
        //文件类型
        String fileType = OSSUtils.getFileType(filename);
        //通过响应头告诉客户端返回的文件类型
        resp.setContentType(fileType);
        /*
        还要告诉客户端这是用来下载的.attachment表示一个附件，filename后面跟完整的文件名.URLEncoder.encode使得中文可以在IE和谷歌中存在
        火狐需要用base64编码
         */
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
        //7.文件内容
        String filePath = fileUrl.replace("https://"+OSSUtils.bucketName+"."+OSSUtils.endpoint+"/","");
        OSS ossClient = new OSSClientBuilder().build(OSSUtils.endpoint, OSSUtils.accessKeyId, OSSUtils.accessKeySecret);
        //获取OSS文件对象
        OSSObject ossObject = ossClient.getObject(OSSUtils.bucketName,filePath);
        InputStream inputStream = ossObject.getObjectContent();
        OutputStream outputStream = null;  //引用输出流
        outputStream = resp.getOutputStream();
        //文件流传给客户端

        IOUtils.copy(inputStream, outputStream);   //将数据流复制到输出流，输出给客户端
        //关闭流
        inputStream.close();
        outputStream.close();
        ossClient.shutdown();
    }

    /**
     * 删除文件
     */
    public ApiResult deleteFile(@RequestParam("fileUrl")String fileUrl){
        //删除OSS中的文件
        OSSUtils.deleteFile(fileUrl);
        //删除文件在数据库中的记录,为了方便可以直接在expscore和reference两张表都执行删除操作
        instructorService.deleteReference(fileUrl);
        studentService.deleteCommit(fileUrl);
        return ApiResultHandler.success(null);
    }
}
