package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.model.CtsRelationGroup;

public interface RelationGroupRepository extends CrudRepository<CtsRelationGroup,Long>  {

    List<CtsRelationGroup> findByWs(String ws);

    CtsRelationGroup findByWsAndGroupName(String ws, String groupName);
    
}
