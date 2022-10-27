package net.ttcxy.chat.contorller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.extra.spring.SpringUtil;

public class BaseController {

    static Map<String,String> map = new HashMap<>();
    
    public static String getUsername(){
        HttpServletRequest request = SpringUtil.getBean(HttpServletRequest.class);
        String token = request.getHeader("token");
        return map.get(token);
    }
}
