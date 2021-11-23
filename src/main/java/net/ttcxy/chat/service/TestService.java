package net.ttcxy.chat.service;

import net.ttcxy.chat.db.Tables;
import static net.ttcxy.chat.db.tables.Member.MEMBER;
import static net.ttcxy.chat.db.tables.Group.GROUP;

import net.ttcxy.chat.db.tables.records.GroupRecord;
import net.ttcxy.chat.db.tables.records.MemberRecord;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestService {

    @Autowired
    DSLContext create;

    public void test(){

//        int execute1 = create.insertInto(Tables.GROUP)
//                .columns(GROUP.ID, GROUP.NAME, GROUP.CREATE_TIME)
//                .values(1, "Java学习交流群", LocalDateTime.now()).execute();

        int execute2 = create.insertInto(Tables.MEMBER)
                .columns(MEMBER.ID, MEMBER.USERNAME, MEMBER.MAIL, MEMBER.NICKNAME, MEMBER.PASSWORD)
                .values("1", "2", "3", "4", "5").execute();

//        System.out.println(execute1);
        System.out.println(execute2);
    }
}
