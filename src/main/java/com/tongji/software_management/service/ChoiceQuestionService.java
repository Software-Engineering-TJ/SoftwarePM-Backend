package com.tongji.software_management.service;

import com.tongji.software_management.dao.ChoiceQuestionRepository;
import com.tongji.software_management.entity.DBEntity.ChoiceQuestion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ChoiceQuestionService {
    @Resource
    private ChoiceQuestionRepository choiceQuestionRepository;

    public ChoiceQuestion get(int id) {
        return choiceQuestionRepository.findChoiceQuestionByChoiceId(id);
    }

}
