package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Instructor;
import com.tongji.software_management.entity.LogicalEntity.DBInstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, String> {
    @Query("select new com.tongji.software_management.entity.LogicalEntity.DBInstructor" +
            "(ins.instructorNumber,ins.email,ins.password,ins.name,ins.sex,ins.phoneNumber,ins.status) " +
            "from Instructor ins where ins.instructorNumber = ?1 and ins.password=?2")
    DBInstructor findDBInstructorByInstructorNumberAndPassword(String instructorNumber, String password);
    default Instructor findInstructorByInstructorNumberAndPassword(String instructorNumber, String password){
        Instructor instructor = new Instructor();
        DBInstructor dbInstructor = findDBInstructorByInstructorNumberAndPassword(instructorNumber, password);
        if(dbInstructor!=null){
            BeanUtils.copyProperties(dbInstructor,instructor);
            return instructor;
        }
        return null;
    }

    @Query("select new com.tongji.software_management.entity.LogicalEntity.DBInstructor" +
            "(ins.instructorNumber,ins.email,ins.password,ins.name,ins.sex,ins.phoneNumber,ins.status) " +
            "from Instructor ins where ins.email = ?1")
    DBInstructor findDBInstructorByEmail(String email);
    default Instructor findInstructorByEmail(String email){
        Instructor instructor = new Instructor();
        DBInstructor dbInstructor = findDBInstructorByEmail(email);
        if(dbInstructor!=null){
            BeanUtils.copyProperties(dbInstructor,instructor);
            return instructor;
        }
        return null;
    }

    @Query("select new com.tongji.software_management.entity.LogicalEntity.DBInstructor" +
            "(ins.instructorNumber,ins.email,ins.password,ins.name,ins.sex,ins.phoneNumber,ins.status) " +
            "from Instructor ins where ins.instructorNumber = ?1")
    DBInstructor findDBInstructorByInstructorNumber(String instructorNumber);
    default Instructor findInstructorByInstructorNumber(String instructorNumber){
        Instructor instructor = new Instructor();
        DBInstructor dbInstructor = findDBInstructorByInstructorNumber(instructorNumber);
        if(dbInstructor!=null){
            BeanUtils.copyProperties(dbInstructor,instructor);
            return instructor;
        }
        return null;
    }

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
