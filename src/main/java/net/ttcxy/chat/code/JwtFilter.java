package net.ttcxy.chat.code;

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
import net.ttcxy.chat.entity.CtsMember;

import org.springframework.util.AntPathMatcher;

@WebFilter(urlPatterns = "/*", filterName = "allFilter")
public class JwtFilter implements Filter {

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
        
        for (String url : openUrls) {
            if(matcher.match(url,requestURI)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        String token = request.getHeader("token");
        CtsMember user = ApplicationData.tokenMemberMap.get(token);

        if(user == null){
            response.setStatus(401);
            return;
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

}
