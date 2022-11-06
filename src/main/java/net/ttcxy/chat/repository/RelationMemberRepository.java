package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.ttcxy.chat.entity.model.CtsRelationMember;

@Repository
public interface RelationMemberRepository extends CrudRepository<CtsRelationMember,Long>  {

    List<CtsRelationMember> findByUsername(String username);
    
}
