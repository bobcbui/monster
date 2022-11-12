package net.ttcxy.chat.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.ttcxy.chat.entity.model.CtsUser;

@Repository
public interface UserRepository extends CrudRepository<CtsUser,String>  {
    
}
