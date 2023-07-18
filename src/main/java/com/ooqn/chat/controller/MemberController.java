package com.ooqn.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ooqn.chat.entity.CtsMember;
import com.ooqn.chat.repository.MemberRepository;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("{username}/agree")
    public JSONObject agree(@PathVariable("username") String username, @RequestParam("checkUrl") String checkUrl){
        CtsMember member = memberRepository.findByUsername(username);
        JSONObject jsonObject = new JSONObject();
        if(member == null){
            jsonObject.put("message", "用户不存在！");
            return jsonObject;
        } 
        

        

        return jsonObject;
    }

    
}
