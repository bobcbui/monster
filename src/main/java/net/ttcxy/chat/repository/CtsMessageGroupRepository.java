package net.ttcxy.chat.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.ttcxy.chat.entity.model.CtsMessageGroup;


@Repository
public interface CtsMessageGroupRepository extends CrudRepository<CtsMessageGroup, Long> {


}
