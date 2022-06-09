package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Student;
import com.tongji.software_management.entity.LogicalEntity.DBStudent;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    @Transactional
    @Modifying
    @Query("update Student set email =?2, name =?3, sex =?4, phoneNumber =?5 where studentNumber =?1")
    int updateStudent(String studentNumber, String email, String name, Integer sex, String phoneNumber);

    @Transactional
    @Modifying
    @Query("update Student set email =?2, phoneNumber =?3 where studentNumber =?1")
    int updateStudent(String studentNumber, String email, String phoneNumber);

    @Query("select new com.tongji.software_management.entity.LogicalEntity.DBStudent" +
            "(s.studentNumber,s.email,s.password,s.name,s.sex,s.phoneNumber,s.status) " +
            "from Student s where s.studentNumber = ?1 and s.password=?2")
    DBStudent findDBStudentByStudentNumberAndPassword(String studentNumber, String password);
    default Student findStudentByStudentNumberAndPassword(String studentNumber, String password){
        Student student = new Student();
        DBStudent dbStudent = findDBStudentByStudentNumberAndPassword(studentNumber, password);
        if(dbStudent!=null){
            BeanUtils.copyProperties(dbStudent,student);
            return student;
        }
        return null;
    }


    @Query("select new com.tongji.software_management.entity.LogicalEntity.DBStudent" +
            "(s.studentNumber,s.email,s.password,s.name,s.sex,s.phoneNumber,s.status) " +
            "from Student s where s.email = ?1")
    DBStudent findDBStudentByEmail(String email);
    default Student findStudentByEmail(String email){
        Student student = new Student();
        DBStudent dbStudent = findDBStudentByStudentNumber(email);
        if(dbStudent!=null){
            BeanUtils.copyProperties(dbStudent,student);
            return student;
        }
        return null;
    }

    @Query("select new com.tongji.software_management.entity.LogicalEntity.DBStudent" +
            "(s.studentNumber,s.email,s.password,s.name,s.sex,s.phoneNumber,s.status) " +
            "from Student s where s.studentNumber = ?1")
    DBStudent findDBStudentByStudentNumber(String studentNumber);
    default Student findStudentByStudentNumber(String studentNumber){
        Student student = new Student();
        DBStudent dbStudent = findDBStudentByStudentNumber(studentNumber);
        if(dbStudent!=null){
            BeanUtils.copyProperties(dbStudent,student);
            return student;
        }
        return null;
    }

    int deleteStudentByEmail(String email);

    @Transactional
    @Modifying
    @Query("update Student set status=?2 where email=?1")
    int SetStatus(String email,int status);

    @Transactional
    @Modifying
    @Query("update Student set name=?2 where email=?1")
    int SetNickname(String email, String name);

    @Transactional
    @Modifying
    @Query("update Student set password=?2 where studentNumber=?1")
    int UpdatePasswordByStudentNumber(String studentNumber, String password);
}
