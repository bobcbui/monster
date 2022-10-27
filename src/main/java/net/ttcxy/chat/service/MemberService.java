package net.ttcxy.chat.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.crypto.digest.BCrypt;
import net.ttcxy.chat.entity.model.CtsMember;
import net.ttcxy.chat.repository.CtsMemberRepository;

/**
 * @author huanglei
 */
@Service
public class MemberService {

    @Autowired
    CtsMemberRepository memberRepository;

    public CtsMember findByUsername(String username){
        return memberRepository.findByUsername(username);
    }


    public CtsMember save(CtsMember member) {
        return memberRepository.save(member);
    }
}
