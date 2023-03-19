package net.ttcxy.chat.repository;


import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsGroup;

public interface GroupRepository extends CrudRepository<CtsGroup,String>  {

    CtsGroup findByName(String groupName);
    
}
