package net.ttcxy.chat.dsl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import net.ttcxy.chat.entity.model.CtsMember;
import net.ttcxy.chat.entity.model.QCtsMember;

@Component
public class MemberDsl {

    @Autowired
    private JPAQueryFactory query;

    QCtsMember qMember = QCtsMember.ctsMember;

    public CtsMember findByUsername(String username) {
        return query
                .selectFrom(QCtsMember.ctsMember)
                .where(QCtsMember.ctsMember.username.eq(username))
                .fetchOne();
    }

    public long save(CtsMember member) {
        return query
                .insert(qMember)
                .set(qMember.id, UUID.randomUUID().toString())
                .set(qMember.username, member.getUsername())
                .set(qMember.password, member.getPassword())
                .set(qMember.createTime, member.getCreateTime())
                .execute();
    }

}
