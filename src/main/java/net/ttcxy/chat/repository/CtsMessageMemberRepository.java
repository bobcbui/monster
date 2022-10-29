package net.ttcxy.chat.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.ttcxy.chat.entity.model.CtsMember;
import net.ttcxy.chat.entity.model.CtsMessageMember;


@Repository
public interface CtsMessageMemberRepository extends CrudRepository<CtsMessageMember, Long> {


}
