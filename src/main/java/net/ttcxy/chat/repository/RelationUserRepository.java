package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.model.CtsRelationUser;

public interface RelationUserRepository extends CrudRepository<CtsRelationUser,Long>  {

    List<CtsRelationUser> findByUsername(String username);

    List<CtsRelationUser> findByWs(String ws);
    
}
