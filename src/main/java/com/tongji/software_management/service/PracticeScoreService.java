package com.tongji.software_management.service;

import com.tongji.software_management.dao.PracticeRepository;
import com.tongji.software_management.dao.PracticeScoreRepository;
import com.tongji.software_management.entity.DBEntity.PracticeScore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PracticeScoreService {
    @Resource
    private PracticeScoreRepository practiceScoreRepository;

    public void insert (PracticeScore practiceScore) {
        practiceScoreRepository.save(practiceScore);
    }
}
