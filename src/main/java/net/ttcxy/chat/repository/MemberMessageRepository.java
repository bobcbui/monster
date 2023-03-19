package net.ttcxy.chat.repository;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsMemberMessage;

public interface MemberMessageRepository extends CrudRepository<CtsMemberMessage,String>  {

}
