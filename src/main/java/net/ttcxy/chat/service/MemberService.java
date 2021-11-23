package net.ttcxy.chat.service;


import cn.hutool.core.util.IdUtil;
import net.ttcxy.chat.db.Tables;
import net.ttcxy.chat.db.tables.pojos.Member;
import net.ttcxy.chat.db.tables.records.MemberRecord;
import net.ttcxy.chat.entity.CurrentMember;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.ttcxy.chat.db.tables.Member.MEMBER;

/**
 * @author huanglei
 */
@Service
public class MemberService implements UserDetailsService {

    private static Map<String, Member> memberMap = new HashMap<>();

    static {
        Member member1 = new Member();
        member1.setId("1");
        member1.setUsername("user1");
        member1.setNickname("user1");
        member1.setMail("792190997@qq.com");
        member1.setPassword("$2a$10$WiuebtAwq/sg5ivzD9Z4k.eHdYonJpJ6C4K6/6SypWRHBEPfWB1hq");

        Member member2 = new Member();
        member2.setId("2");
        member2.setUsername("user2");
        member2.setNickname("user1");
        member2.setMail("17674785177@qq.com");
        member2.setPassword("$2a$10$WiuebtAwq/sg5ivzD9Z4k.eHdYonJpJ6C4K6/6SypWRHBEPfWB1hq");
        memberMap.put("792190997@qq.com",member1);
        memberMap.put("17674785177@qq.com",member1);
    }

    @Autowired
    private MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails details = memberService.selectByMail(username);
        if (details != null){
            return memberService.selectByMail(username);
        }
        throw new UsernameNotFoundException("用户不存在");
    }

    @Autowired
    DSLContext create;

    public int insertMember(Member member) {
        return create.insertInto(Tables.MEMBER)
                .columns(MEMBER.ID,
                        MEMBER.USERNAME,
                        MEMBER.NICKNAME,
                        MEMBER.MAIL,
                        MEMBER.PASSWORD,
                        MEMBER.CREATE_TIME)
                .values(IdUtil.objectId(),
                        member.getUsername(),
                        member.getNickname(),
                        member.getMail(),
                        member.getPassword(),
                        LocalDateTime.now()).execute();
    }

    public UserDetails selectByMail(String mail) {
        List<Member> members = create.select().from(MEMBER).where(MEMBER.MAIL.eq(mail)).fetchInto(Member.class);
        for (Member member : members) {
            CurrentMember currentMember = new CurrentMember();
            currentMember.setMember(member);
            return currentMember;
        }
        return null;
    }


}
