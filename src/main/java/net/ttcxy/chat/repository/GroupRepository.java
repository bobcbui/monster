package net.ttcxy.chat.repository;


import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.model.CtsGroup;

public interface GroupRepository extends CrudRepository<CtsGroup,Long>  {

    CtsGroup findByGroupName(String groupName);
    
}
