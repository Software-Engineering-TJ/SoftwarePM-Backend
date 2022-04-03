package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends JpaRepository<Counter, Integer> {
}
