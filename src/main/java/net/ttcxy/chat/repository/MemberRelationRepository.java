package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsMemberRelation;

public interface MemberRelationRepository extends CrudRepository<CtsMemberRelation,Long>  {

    List<CtsMemberRelation> findByUsername(String username);

    List<CtsMemberRelation> findByWs(String ws);
    
}
