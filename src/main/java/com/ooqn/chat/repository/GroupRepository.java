package com.ooqn.chat.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ooqn.chat.entity.CtsGroup;

public interface GroupRepository extends CrudRepository<CtsGroup,String>  {

    CtsGroup findByName(String groupName);

    @Query(value = "select * from cts_group where id in (select group_id from cts_group_relation where member_account = ?1)",nativeQuery = true)
    List<CtsGroup> findByMemberAccount(String account);

    @Query(value = "select * from cts_group limit 10",nativeQuery = true)
    List<CtsGroup> findLimit10();
    
}
