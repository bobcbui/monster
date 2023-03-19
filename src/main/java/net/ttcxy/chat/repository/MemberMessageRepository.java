package net.ttcxy.chat.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsMemberMessage;

public interface MemberMessageRepository extends CrudRepository<CtsMemberMessage,Long>  {

    List<CtsMemberMessage> findByNameAndTypeOrderByCreateTime(String groupName, String string);

}
