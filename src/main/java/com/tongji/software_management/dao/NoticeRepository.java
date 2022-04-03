package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Notice;
import com.tongji.software_management.entity.DBEntity.NoticeEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, NoticeEntityPK> {
}
