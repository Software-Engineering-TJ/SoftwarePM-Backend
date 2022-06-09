package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Administrator;
import com.tongji.software_management.entity.LogicalEntity.DBAdministrator;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, String> {

   @Query("select new com.tongji.software_management.entity.LogicalEntity.DBAdministrator" +
           "(a.adminNumber,a.email,a.password,a.name) " +
           "from Administrator a where a.adminNumber = ?1")
   DBAdministrator findDBAdministratorByAdminNumber(String adminNumber);
   default Administrator findAdministratorByAdminNumber(String adminNumber){
      DBAdministrator dbAdministrator = findDBAdministratorByAdminNumber(adminNumber);
      Administrator administrator = new Administrator();
      BeanUtils.copyProperties(administrator,dbAdministrator);
      return administrator;
   }

   @Query("select new com.tongji.software_management.entity.LogicalEntity.DBAdministrator" +
           "(a.adminNumber,a.email,a.password,a.name) " +
           "from Administrator a where a.adminNumber = ?1 and a.password=?2")
   DBAdministrator findDBAdministratorByAdminNumberAndPassword(String adminNumber, String password);
   default Administrator findAdministratorByAdminNumberAndPassword(String adminNumber, String password){
      DBAdministrator dbAdministrator = findDBAdministratorByAdminNumberAndPassword(adminNumber, password);
      Administrator administrator = new Administrator();
      BeanUtils.copyProperties(administrator,dbAdministrator);
      return administrator;
   }

   @Query("select new com.tongji.software_management.entity.LogicalEntity.DBAdministrator" +
           "(a.adminNumber,a.email,a.password,a.name) " +
           "from Administrator a where a.email = ?1")
   DBAdministrator findDBAdministratorByEmail(String email);
   default Administrator findAdministratorByEmail(String email){
      DBAdministrator dbAdministrator = findDBAdministratorByEmail(email);
      Administrator administrator = new Administrator();
      BeanUtils.copyProperties(administrator,dbAdministrator);
      return administrator;
   }

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
