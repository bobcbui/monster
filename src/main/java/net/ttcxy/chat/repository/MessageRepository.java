package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.model.CtsMessageUser;

public interface MessageRepository extends CrudRepository<CtsMessageUser,Long>  {

    List<CtsMessageUser> findByNameAndTypeOrderByCreateTime(String groupName, String string);

}
