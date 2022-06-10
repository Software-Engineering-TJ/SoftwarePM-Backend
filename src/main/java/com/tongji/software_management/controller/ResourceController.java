package com.tongji.software_management.controller;

import com.tongji.software_management.entity.DBEntity.Resource;
import com.tongji.software_management.entity.LogicalEntity.ApiResult;
import com.tongji.software_management.entity.LogicalEntity.ResourceDTO;
import com.tongji.software_management.service.ResourceService;
import com.tongji.software_management.utils.ApiResultHandler;
import com.tongji.software_management.utils.OSSUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Api(tags="新增的\"多模式教学\"模块")
@CrossOrigin
@RestController
@RequestMapping("resource")
public class ResourceController {
    @Autowired
    ResourceService resourceService;

    private static final String RESOURCE_PATH = "softwareManagement/resource/";

    // 分页查询班级资源
    @GetMapping("{pageNumber}/{pageSize}")
    public ApiResult getResourceList(@RequestParam("courseId") String courseId,
                                     @RequestParam("classId")String classId,
                                     @RequestParam("type")int type,
                                     @PathVariable("pageNumber") int pageNumber,
                                     @PathVariable("pageSize") int pageSize) {
        if(pageNumber < 1)
            return ApiResultHandler.fail("页号不合适");
        if(pageSize < 1)
            return ApiResultHandler.fail("页大小不合适");

        List<Resource> resourceList = resourceService.findByCourseIdAndClassIdAndTypeAndLimit(courseId,classId,type,pageNumber,pageSize);

        List<ResourceDTO> resourceInfoList  = new ArrayList<>();
        for(Resource resource : resourceList){
            ResourceDTO resourceInfo = resourceService.convertToResouceDTO(resource);
            resourceInfoList.add(resourceInfo);
        }

        int count = resourceService.getCountOfResourceByCourseIdAndClassIdAndType(courseId,classId,type);
        int temp = count/pageSize;
        int pageCount = (count%pageSize==0)?temp:temp+1;

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageCount);
        map.put("resourceInfoList",resourceInfoList);

        return ApiResultHandler.success(map);
    }

    // 资源-增
    @PostMapping
    public ApiResult uploadResource(@RequestParam("courseId")String courseId,
                                    @RequestParam("classId")String classId,
                                    @RequestParam("instructorNumber")String instructorNumber,
                                    @RequestParam("title")String title,
                                    @RequestParam("type")int type,
                                    @RequestParam("file")MultipartFile file){
        // 上传文件
        String filePath = RESOURCE_PATH+courseId+"/"+classId+"/"+file.getOriginalFilename();
        String fileUrl = OSSUtils.uploadFile(filePath,file);

        if(fileUrl.equals("failed")){
            return ApiResultHandler.fail("文件上传失败");
        }

        Resource resource = new Resource();
        resource.setCourseId(courseId);
        resource.setClassId(classId);
        resource.setInstructorNumber(instructorNumber);
        resource.setTitle(title);
        resource.setType(type);
        resource.setUrl(fileUrl);

        int resourceId = resourceService.createResource(resource);

        return ApiResultHandler.success(resourceId);
    }

    // 资源-删
    @DeleteMapping
    public ApiResult deleteResource(@RequestParam("resourceId")int resourceId){
        resourceService.deleteResource(resourceId);

        return ApiResultHandler.success(resourceId);
    }
}
