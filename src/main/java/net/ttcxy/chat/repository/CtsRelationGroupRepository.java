package net.ttcxy.chat.repository;


import net.ttcxy.chat.entity.model.CtsMember;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CtsRelationGroupRepository extends CrudRepository<CtsRelationGroup, Long> {
    CtsRelationGroup findByGroupUrlAndMemberUrl(String groupUrl, String memberUrl);
}
