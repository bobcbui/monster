package net.ttcxy.chat.dsl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import net.ttcxy.chat.entity.dto.GroupDto;
import net.ttcxy.chat.entity.model.CtsGroup;
import net.ttcxy.chat.entity.model.QCtsGroup;
import net.ttcxy.chat.entity.model.QCtsMemberGroup;

@Component
public class GroupDsl {

    @Autowired
    private JPAQueryFactory query;

    QCtsGroup qGroup = QCtsGroup.ctsGroup;
    QCtsMemberGroup qMemberGroup = QCtsMemberGroup.ctsMemberGroup;

    public List<GroupDto> getGroupByMemberId() {
        return query
                .select(Projections.bean(
                        GroupDto.class,
                        qGroup.nickname,
                        qGroup.createTime,
                        qGroup.createMemberId,
                        qMemberGroup.groupId))
                .from(qGroup, qMemberGroup)
                .where(qGroup.id.eq(qMemberGroup.groupId))
                .fetch();
    }

    public long save(CtsGroup group) {
        group.setId(UUID.randomUUID().toString());
        return query
                .insert(qGroup)
                .set(qGroup.nickname, group.getNickname())
                .set(qGroup.createMemberId, group.getCreateMemberId())
                .set(qGroup.createTime, group.getCreateTime())
                .execute();
    }
    

}
