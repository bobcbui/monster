package com.ooqn.chat.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ooqn.chat.entity.CtsGroupRelation;

import cn.hutool.core.date.DateTime;
import jakarta.transaction.Transactional;

public interface GroupRelationRepository extends CrudRepository<CtsGroupRelation,String>  {

    CtsGroupRelation findByMemberAccountAndGroupAccount(String memberAccount, String groupAccount);

    List<CtsGroupRelation> findByMemberAccount(String account);

    @Query(value = "update cts_group_relation set last_read_time = ?3 where member_account = ?1 and group_account = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateReadTime(String account, String id, DateTime date);


}
