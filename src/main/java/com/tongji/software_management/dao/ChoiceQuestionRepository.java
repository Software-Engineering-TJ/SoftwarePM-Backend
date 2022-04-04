package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.ChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceQuestionRepository extends JpaRepository<ChoiceQuestion, Integer> {
    ChoiceQuestion findChoiceQuestionByChoiceId(int id);
}
