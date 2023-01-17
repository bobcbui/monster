package net.ttcxy.chat.repository;


import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.model.CtsUser;

public interface UserRepository extends CrudRepository<CtsUser,String>  {
    
}
