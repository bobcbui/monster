package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.model.CtsMessage;

public interface MessageRepository extends CrudRepository<CtsMessage,Long>  {

    List<CtsMessage> findByNameAndTypeOrderByCreateTime(String groupName, String string);

    List<CtsMessage> findByNameAndWs(String username, String ws);
    
}
