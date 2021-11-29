package net.ttcxy.chat.service;


import net.ttcxy.chat.entity.CurrentMember;
import net.ttcxy.chat.entity.model.Member;
import net.ttcxy.chat.entity.model.MemberExample;
import net.ttcxy.chat.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huanglei
 */
@Service
public class MemberService implements UserDetailsService {


    private final MemberMapper memberMapper;

    @Autowired
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
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
        return memberMapper.insert(member);
    }

    public UserDetails selectMemberByName(String username) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andUsernameEqualTo(username);
        List<Member> members = memberMapper.selectByExample(memberExample);
        if (members.size() > 0){
            CurrentMember currentMember = new CurrentMember();
            currentMember.setMember(members.get(0));
            return currentMember;
        }
        return null;
    }
}
