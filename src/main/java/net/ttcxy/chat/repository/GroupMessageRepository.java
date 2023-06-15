package net.ttcxy.chat.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsGroupMessage;

public interface GroupMessageRepository extends CrudRepository<CtsGroupMessage,String>{

    List<CtsGroupMessage> findByAcceptGroupId(String id);
    
}
