package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, String> {
   Administrator findAdministratorByAdminNumber(String adminNumber);
   Administrator findAdministratorByAdminNumberAndPassword(String adminNumber, String password);
   Administrator findAdministratorByEmail(String email);
   int deleteAdministratorByEmail(String email);

   @Transactional
   @Modifying
   @Query("update Administrator ad set ad.name = ?2 where (ad.email = ?1)")
   int SetNickname(String email, String name);

   @Transactional
   @Modifying
   @Query("update Administrator ad set ad.password = ?2 where (ad.adminNumber = ?1)")
   int UpdatePasswordByAdminNumber(String adminNumber, String password);

}
