package com.ooqn.chat.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ooqn.chat.entity.CtsVerify;

public interface VerifyRepository extends CrudRepository<CtsVerify,String> {

    List<CtsVerify> findByMemberId(String id);
    
}
