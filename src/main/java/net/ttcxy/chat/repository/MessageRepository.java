package net.ttcxy.chat.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.ttcxy.chat.entity.model.CtsMessage;

@Repository
public interface MessageRepository extends CrudRepository<CtsMessage,Long>  {
    
}
