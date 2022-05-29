package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Notice;
import com.tongji.software_management.entity.DBEntity.NoticePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, NoticePK> {
    List<Notice> findNoticesByCourseIdAndClassId(String courseID, String classID);
    int deleteNoticeByCourseIdAndClassIdAndInstructorNumberAndDate(String courseID, String classID, String instructorNumber, String date);

}
