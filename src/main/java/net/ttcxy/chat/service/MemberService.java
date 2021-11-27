package net.ttcxy.chat.service;


import cn.hutool.core.util.IdUtil;
import net.ttcxy.chat.db.Tables;
import net.ttcxy.chat.db.tables.pojos.Member;
import net.ttcxy.chat.db.tables.records.MemberRecord;
import net.ttcxy.chat.entity.CurrentMember;
import org.jooq.DSLContext;
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

    private final DSLContext create;

    @Autowired
    public MemberService(DSLContext create) {
        this.create = create;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails details = selectMemberByName(username);
        if (details != null){
            return details;
        }
        throw new UsernameNotFoundException("用户不存在");
    }

    public int insertMember(Member member) {
        return create.insertInto(Tables.MEMBER)
                .columns(MEMBER.ID,
                        MEMBER.USERNAME,
                        MEMBER.PASSWORD,
                        MEMBER.CREATE_TIME)
                .values(IdUtil.objectId(),
                        member.getUsername(),
                        member.getPassword(),
                        LocalDateTime.now()).execute();
    }

    public UserDetails selectMemberByName(String username) {
        List<Member> members = create.select().from(MEMBER).where(MEMBER.USERNAME.eq(username)).fetchInto(Member.class);
        for (Member member : members) {
            CurrentMember currentMember = new CurrentMember();
            currentMember.setMember(member);
            return currentMember;
        }
        return null;
    }
}
