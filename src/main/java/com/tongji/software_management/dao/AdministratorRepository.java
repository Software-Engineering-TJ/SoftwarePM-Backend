package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Administrator;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, String> {
   public Administrator findAdministratorByAdminNumber(String adminNumber);
   public Administrator findAdministratorByAdminNumberAndPassword(String adminNumber, String password);
   public Administrator findAdministratorByEmail(String email);

}
