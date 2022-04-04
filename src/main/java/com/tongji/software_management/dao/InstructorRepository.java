package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, String> {
    Instructor findInstructorByInstructorNumberAndPassword(String instructorNumber, String password);
    Instructor findInstructorByEmail(String email);
    Instructor findInstructorByInstructorNumber(String instructorNumber);
    int deleteInstructorByEmail(String email);

    @Transactional
    @Modifying
    @Query("update Instructor i set i.status = ?2 where (i.email = ?1)")
    int SetStatus(String email,int status);

    @Transactional
    @Modifying
    @Query("update Instructor i set i.name = ?2 where (i.email = ?1)")
    int SetNickname(String email, String name);

    @Transactional
    @Modifying
    @Query("update Instructor i set i.email = ?2, i.phoneNumber = ?3 where (i.instructorNumber = ?1)")
    int updateInstructor(String instructorNumber, String email, String phoneNumber);

    @Transactional
    @Modifying
    @Query("update Instructor i set i.email = ?2, i.name = ?3, i.sex = ?4, i.phoneNumber = ?5 where (i.instructorNumber = ?1)")
    int updateInstructor(String instructorNumber, String email, String name, Integer sex, String phoneNumber);

    @Transactional
    @Modifying
    @Query("update Instructor i set i.password = ?2 where (i.instructorNumber = ?1)")
    int UpdatePasswordByInstructorNumber(String instructorNumber,String password);


}
