package com.ooqn.chat.code;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.ooqn.chat.entity.CtsMember;

import org.springframework.util.AntPathMatcher;

@WebFilter(urlPatterns = "/*", filterName = "allFilter")
public class TokenFilter implements Filter {

    private final static AntPathMatcher matcher = new AntPathMatcher();  

    @Override
    public void init(FilterConfig filterConfig) {
        
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String requestURI = request.getRequestURI();

        if (!requestURI.contains("api")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 是否不需要登陆
        String[] openUrls = new String[]{"","/","/*","/ws/*","/group/create","/username/*/token/*"};
        
        // 白名单
        for (String url : openUrls) {
            if(matcher.match(url,requestURI)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        CtsMember member = ApplicationData.tokenMemberMap.get(request.getHeader("token"));

        // 未登录
        if(member == null){
            response.setStatus(401);
            return;
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

}
