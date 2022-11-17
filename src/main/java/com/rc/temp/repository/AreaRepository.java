package com.rc.temp.repository;

import com.rc.temp.db.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area,Integer>, QuerydslPredicateExecutor<Area> {

    @Query(value = "select a.id from Area a")
    List<Integer> getIds();
}
