package net.ttcxy.chat.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.IdUtil;
import net.ttcxy.chat.code.api.ApiException;
import net.ttcxy.chat.entity.model.CtsMember;
import net.ttcxy.chat.entity.param.RegisterParam;
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
        CtsMember member = memberService.findByUsername(username);
        if(member.getPassword().equals(password)){
            return IdUtil.fastSimpleUUID();
        }
        throw new ApiException("密码或用户名不正确！");
    }

    @PostMapping("register")
    public String register(@RequestBody RegisterParam param){
        return "注册成功";
    }

}
