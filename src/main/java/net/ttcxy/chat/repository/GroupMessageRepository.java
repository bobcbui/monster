package net.ttcxy.chat.repository;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsGroupMessage;

public interface GroupMessageRepository extends CrudRepository<CtsGroupMessage,String>{
    
}
