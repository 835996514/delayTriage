package com.rc.temp.repository;

import com.rc.temp.db.PatientRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRegisterRepository extends JpaRepository<PatientRegister,String>, QuerydslPredicateExecutor<PatientRegister> {

    @Query(value = "select 1 from triage_patient_queue where REGISTER_ID = :registerId",nativeQuery = true)
    Integer existQueue(@Param("registerId") String registerId);
}
