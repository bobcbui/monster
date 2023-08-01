package com.ooqn.chat.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ooqn.chat.entity.CtsMemberRelation;

import cn.hutool.core.date.DateTime;
import jakarta.transaction.Transactional;

public interface MemberRelationRepository extends CrudRepository<CtsMemberRelation,String>  {

    List<CtsMemberRelation> findByMemberIdAndState(String id, int i);

    List<CtsMemberRelation> findByAccount(String account);

    @Query(value = "delete from cts_member_relation where member_id = ?1 and account = ?2",nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByMemberIdAndAccount(String memberId, String account);

    CtsMemberRelation findByMemberIdAndAccount(String id, String account);

    @Query(value = "update cts_member_relation set read_time = ?3 where account = ?1 and member_id = ?2",nativeQuery = true)
    @Modifying
    @Transactional
    void updateReadTime(String account, String id, DateTime date);
    
    
}
