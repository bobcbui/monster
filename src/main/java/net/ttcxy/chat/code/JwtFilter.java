package net.ttcxy.chat.code;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;

import net.ttcxy.chat.entity.model.CtsUser;

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
        CtsUser user = ApplicationData.tokenUserMap.get(token);

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
