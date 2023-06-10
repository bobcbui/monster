package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsMember;

public interface MemberRepository extends CrudRepository<CtsMember,String>  {

    CtsMember findByUsername(String name);

    @Query(value = "select * from cts_member where id in (select member_id from cts_member_relation where member_id = ?1)",nativeQuery = true)
    List<CtsMember> findMemberByMemberId(String memberId);
    
}
