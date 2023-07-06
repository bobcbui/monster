package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import net.ttcxy.chat.entity.CtsMemberRelation;

public interface MemberRelationRepository extends CrudRepository<CtsMemberRelation,String>  {

    List<CtsMemberRelation> findByMemberId(String memberId);

    List<CtsMemberRelation> findByAccount(String account);

    @Query(value = "delete from cts_member_relation where member_id = ?1 and account = ?2",nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByMemberIdAndAccount(String memberId, String account);
    
}
