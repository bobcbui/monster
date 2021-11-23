package net.ttcxy.chat.service;

import net.ttcxy.chat.db.Tables;
import static net.ttcxy.chat.db.tables.GroupMember.GROUP_MEMBER;

import net.ttcxy.chat.db.tables.GroupMember;
import net.ttcxy.chat.db.tables.pojos.Group;
import net.ttcxy.chat.db.tables.records.GroupRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    DSLContext create;

    public List<Group> selectAllGroup() {
        List<Group> groups = create.select()
                .from(Tables.GROUP)
                .fetchInto(Group.class);
        for (Group group : groups) {
            System.out.println(group.toString());
        }
        return groups;
    }

    public List<String> selectMemberGroup(String memberId){
        Result<Record1<String>> fetch = create.select(GROUP_MEMBER.GROUP_ID)
                .from(Tables.GROUP_MEMBER).where(GROUP_MEMBER.MEMBER_ID.eq(memberId))
                .groupBy(GROUP_MEMBER.GROUP_ID)
                .fetch();
        List<String> values = fetch.getValues(GROUP_MEMBER.GROUP_ID);
        System.out.println(values);
        return values;
    }
}
