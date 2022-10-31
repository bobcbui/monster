package net.ttcxy.chat.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.ttcxy.chat.entity.model.CtsRelationGroup;


@Repository
public interface RelationGroupRepository extends CrudRepository<CtsRelationGroup, Long> {

}
