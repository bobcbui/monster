package net.ttcxy.chat.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.BCrypt;
import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.code.api.ApiException;
import net.ttcxy.chat.entity.model.CtsMember;
import net.ttcxy.chat.service.MemberService;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    MemberService memberService;

    @PostMapping("/authenticate")
    public String authorize(@RequestBody JSONObject loginParam) {
        String username = loginParam.getString("username");
        String password = loginParam.getString("password");
        password = BCrypt.hashpw(password);
        CtsMember member = memberService.findByUsername(username);
        if(BCrypt.checkpw(loginParam.getString("password"),member.getPassword())){
            String token = IdUtil.fastSimpleUUID();
            ApplicationData.memberMap.put(token, member);
            return token;
        }
        throw new ApiException("密码或用户名不正确！");
    }

    @PostMapping("register")
    public CtsMember register(@RequestBody JSONObject loginParam){
        String username = loginParam.getString("username");
        String password = loginParam.getString("password");
        password = BCrypt.hashpw(password);
        CtsMember member = new CtsMember();
        member.setUsername(username);
        member.setCreateTime(DateUtil.date());
        member.setPassword(password);
        return memberService.save(member);
    }

}
