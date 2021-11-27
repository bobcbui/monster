package net.ttcxy.chat.service;

import net.ttcxy.chat.db.Tables;
import static net.ttcxy.chat.db.tables.Member.MEMBER;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {


    private final DSLContext create;

    @Autowired
    public TestService(DSLContext create) {
        this.create = create;
    }

    public void test(){
        int execute2 = create.insertInto(Tables.MEMBER)
                .columns(MEMBER.ID, MEMBER.USERNAME, MEMBER.PASSWORD)
                .values("1", "2", "5").execute();
        System.out.println(execute2);
    }
}
