package com.rt_chatApp.repository;

import com.rt_chatApp.Model.UsernameGenNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsernameGenRepository extends JpaRepository<UsernameGenNum, Integer> {
}
