package com.ooqn.chat.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ooqn.chat.entity.CtsVerify;

import jakarta.transaction.Transactional;

@Transactional
public interface VerifyRepository extends CrudRepository<CtsVerify,String> {

    List<CtsVerify> findByMemberId(String id);

    void deleteByMemberIdAndId(String id, String string);
    
}
