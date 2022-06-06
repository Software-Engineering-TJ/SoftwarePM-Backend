package com.tongji.software_management.service;

import com.tongji.software_management.dao.InstructorRepository;
import com.tongji.software_management.dao.ResourceRepository;
import com.tongji.software_management.entity.DBEntity.Instructor;
import com.tongji.software_management.entity.DBEntity.Resource;
import com.tongji.software_management.entity.LogicalEntity.ResourceDTO;
import com.tongji.software_management.utils.OSSUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceService {
    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    InstructorRepository instructorRepository;

    public List<Resource> findByCourseIdAndClassIdAndTypeAndLimit(String courseId, String classId,
                                                                  int type, int pageNumber, int pageSize){
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        if(type == -1){
            return resourceRepository.findByCourseIdAndClassId(courseId,classId,pageable).getContent();
        }
        return resourceRepository.findByCourseIdAndClassIdAndType(courseId,classId,type,pageable).getContent();
    }

    public ResourceDTO convertToResouceDTO(Resource resource){
        ResourceDTO resourceDTO = new ResourceDTO();
        BeanUtils.copyProperties(resource,resourceDTO);

        Instructor instructor = instructorRepository.findInstructorByInstructorNumber(resource.getInstructorNumber());
        resourceDTO.setInstructorName(instructor.getName());
        Map<String, String> fileMeta = OSSUtils.getObjectMeta(resource.getUrl());
        resourceDTO.setFileSize(fileMeta.get("fileSize"));

        return resourceDTO;
    }

    public int getCountOfResourceByCourseIdAndClassIdAndType(String courseId, String classId, int type){
        if(type==-1)
            return resourceRepository.countByCourseIdAndClassId(courseId,classId);
        return resourceRepository.countByCourseIdAndClassIdAndType(courseId, classId, type);
    }

    public int createResource(Resource resource){
        return resourceRepository.saveAndFlush(resource).getId();
    }

    public void deleteResource(int resourceId){
        resourceRepository.deleteById(resourceId);
    }
}
