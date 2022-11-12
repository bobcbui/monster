package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.ttcxy.chat.entity.model.CtsRelationUser;

@Repository
public interface RelationUserRepository extends CrudRepository<CtsRelationUser,Long>  {

    List<CtsRelationUser> findByUsername(String username);
    
}
