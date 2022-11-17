package com.rc.temp.repository;

import com.rc.temp.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>, QuerydslPredicateExecutor<User> {

    User getByUsernameEquals(String username);
}
