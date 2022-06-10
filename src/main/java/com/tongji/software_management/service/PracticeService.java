package com.tongji.software_management.service;

import com.tongji.software_management.dao.PracticeRepository;
import com.tongji.software_management.dao.PracticeScoreRepository;
import com.tongji.software_management.entity.DBEntity.Practice;
import com.tongji.software_management.entity.DBEntity.PracticeScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PracticeService {
    @Resource
    private PracticeRepository practiceRepository;

    @Autowired
    private PracticeScoreRepository practiceScoreRepository;

    public Practice get(int practiceId) {
        return practiceRepository.findByPracticeId(practiceId);
    }

    public void add(Practice practice) {
        practiceRepository.save(practice);
    }

    public double getStudentPracticeScore(Practice practice,String studentNumber){
        PracticeScore practiceScore = practiceScoreRepository
                .findPracticeScoreByCourseIdAndClassIdAndPracticeNameAndStudentNumber(
                        practice.getCourseId(),
                        practice.getClassId(),
                        practice.getPracticeName(),
                        studentNumber);
        if(practiceScore==null){
            return 0;
        }else{
            return practiceScore.getIndividualScore();
        }
    }

}
