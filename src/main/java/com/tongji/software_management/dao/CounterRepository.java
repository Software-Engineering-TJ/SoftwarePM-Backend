package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CounterRepository extends JpaRepository<Counter, Integer> {
    Counter findCounterById(int id);

    @Transactional
    @Modifying
    @Query("update Counter c set c.courseId = ?1 , c.classId = ?2 where c.id = 1")
    int UpdateAllOfCounter(int courseID, int classID);

    @Transactional
    @Modifying
    @Query("update Counter c set c.courseId = ?1 where c.id = 1")
    int UpdateCourseIDOfCounter(int courseID);

    @Transactional
    @Modifying
    @Query("update Counter c set c.classId = ?1 where c.id = 1")
    int UpdateClassIDOfCounter(int classID);

}
