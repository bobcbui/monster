package net.ttcxy.chat.code;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ttcxy.chat.code.api.ApiException;
import net.ttcxy.chat.entity.model.CtsMember;
import org.springframework.util.AntPathMatcher;

@WebFilter(urlPatterns = "/*", filterName = "allFilter")
public class JwtFilter implements Filter {

    private final static AntPathMatcher matcher = new AntPathMatcher();  

    private final static Map<String, CtsMember> memberMap = new HashMap<>();

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
        String[] openUrls = new String[]{"/api/authenticate","/api/register"};
        
        for (String url : openUrls) {
            if(matcher.match(url,requestURI)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        String token = request.getHeader("token");
        CtsMember member = memberMap.get(token);

        if(member == null){
            response.sendRedirect("/login.html");
            return;
        }
        request.setAttribute("member",member);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

}
