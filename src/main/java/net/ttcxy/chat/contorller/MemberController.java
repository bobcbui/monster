package net.ttcxy.chat.contorller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.IdUtil;

@RestController
@RequestMapping
public class MemberController {

    @GetMapping("/token")
    public Object token(@RequestParam("memberUrl") String memberUrl,HttpServletRequest request) {
       
        System.out.println(request.getRemoteHost());
        return null;
    }

    @GetMapping("/{username}")
    public String check(@PathVariable("username")String username) {
        String token = IdUtil.fastSimpleUUID();
        return token;
    }
    
}
