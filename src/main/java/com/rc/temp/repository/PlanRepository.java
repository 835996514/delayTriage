package com.rc.temp.repository;

import com.rc.temp.db.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan,String>, QuerydslPredicateExecutor<Plan> {

    @Query("select new Plan(p.planId,p.name) from Plan p where p.date = :today and p.departmentCode in :departmentCode")
    List<Plan> getAllPlans(@Param("today") Date today, @Param("departmentCode") String[] departmentCode);
}
