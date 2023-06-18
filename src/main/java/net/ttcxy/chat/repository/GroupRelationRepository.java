package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.ttcxy.chat.entity.CtsGroupRelation;

public interface GroupRelationRepository extends CrudRepository<CtsGroupRelation,String>  {

    CtsGroupRelation findByMemberAccountAndGroupAccount(String memberAccount, String groupAccount);

    List<CtsGroupRelation> findByMemberAccount(String account);


}
