package net.ttcxy.chat.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.ttcxy.chat.entity.model.CtsGroup;


@Repository
public interface GroupRepository extends CrudRepository<CtsGroup, Long> {

}
