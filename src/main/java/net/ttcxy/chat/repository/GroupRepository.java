package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsGroup;

public interface GroupRepository extends CrudRepository<CtsGroup,String>  {

    CtsGroup findByName(String groupName);

    @Query(value = "select * from cts_group where id in (select group_id from cts_group_relation where member_id = ?1)",nativeQuery = true)
    List<CtsGroup> findGroupByMemberId(String id);

    // 通过 GroupRelation 中的 groupId 查询群组列表
    @Query(value = "select * from cts_group where id in (select group_id from cts_group_relation where member_id = ?1)",nativeQuery = true)
    List<CtsGroup> findByMemberId(String id);
    
}
