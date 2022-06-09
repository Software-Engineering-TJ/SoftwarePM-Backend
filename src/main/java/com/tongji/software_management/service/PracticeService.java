package com.tongji.software_management.service;

import com.tongji.software_management.dao.PracticeRepository;
import com.tongji.software_management.entity.DBEntity.Practice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PracticeService {
    @Resource
    private PracticeRepository practiceRepository;

    public Practice get(String practiceId) {
        return practiceRepository.findByPracticeId(practiceId);
    }

    public void add(Practice practice) {
        practiceRepository.save(practice);
    }



}
