package net.ttcxy.chat.repository;


import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsMember;

public interface MemberRepository extends CrudRepository<CtsMember,String>  {

    CtsMember findByName(String name);
    
}
