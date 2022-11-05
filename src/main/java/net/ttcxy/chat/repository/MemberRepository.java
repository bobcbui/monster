package net.ttcxy.chat.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.ttcxy.chat.entity.model.CtsMember;

@Repository
public interface MemberRepository extends CrudRepository<CtsMember,Long>  {

    CtsMember findByUsername(String username);
    
}
