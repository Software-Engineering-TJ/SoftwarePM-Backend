package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    @Transactional
    @Modifying
    @Query("update Student set email =?2, name =?3, sex =?4, phoneNumber =?5 where studentNumber =?1")
    int updateStudent(String studentNumber, String email, String name, Integer sex, String phoneNumber);
}
