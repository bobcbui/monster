package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsMemberRelation;

public interface MemberRelationRepository extends CrudRepository<CtsMemberRelation,String>  {

    List<CtsMemberRelation> findByMemberId(String memberId);

    List<CtsMemberRelation> findByWs(String ws);
    
}
