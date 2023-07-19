package com.ooqn.chat.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import com.ooqn.chat.entity.CtsMemberRelation;

public interface MemberRelationRepository extends CrudRepository<CtsMemberRelation,String>  {

    List<CtsMemberRelation> findByMemberIdAndState(String id, int i);

    List<CtsMemberRelation> findByAccount(String account);

    @Query(value = "delete from cts_member_relation where member_id = ?1 and account = ?2",nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByMemberIdAndAccount(String memberId, String account);

    CtsMemberRelation findByMemberIdAndAccount(String id, String account);
    
    
}
