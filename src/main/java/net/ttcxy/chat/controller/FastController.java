package net.ttcxy.chat.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.fastjson.JSONObject;
import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.code.api.ApiException;
import net.ttcxy.chat.entity.model.CtsMember;
import net.ttcxy.chat.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping
public class FastController {

    @Autowired
    MemberService memberService;

    @PostMapping("/authenticate")
    public String authorize(@RequestBody JSONObject loginParam) {
        String username = loginParam.getString("username");
        String password = loginParam.getString("password");
        CtsMember member = memberService.findByUsername(username);
        if(BCrypt.checkpw(password, member.getPassword())){
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
        member.setCreateTime(new Date());
        member.setPassword(password);
        return memberService.save(member);
    }

    @GetMapping("token")
    public String token(HttpServletRequest request) {
        String checkToken = IdUtil.fastSimpleUUID();
        String token = request.getHeader("token");
        CtsMember member = ApplicationData.memberMap.get(token);
        ApplicationData.checkToken.put(checkToken,member.getUsername());
       return checkToken;
    }

    @GetMapping("{username}")
    public Boolean check(@PathVariable("username")String username, @RequestParam("token")String token) {
        return Optional
                .ofNullable(ApplicationData.checkToken.get(token))
                .orElse("")
                .equals(username);
    }
}
