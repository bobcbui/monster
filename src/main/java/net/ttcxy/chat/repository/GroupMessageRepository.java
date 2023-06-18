package net.ttcxy.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsGroupMessage;

public interface GroupMessageRepository extends CrudRepository<CtsGroupMessage,String>{

    @Query(value = "select * from cts_group_message where accept_group_id = ?1 order by create_time desc limit 0,20",nativeQuery = true)
    List<CtsGroupMessage> findByAcceptGroupId(String id);


    
}
