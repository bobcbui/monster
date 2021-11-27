package net.ttcxy.chat.security;

import net.ttcxy.chat.security.filter.JwtFilter;
import net.ttcxy.chat.security.jwt.JwtAccessDeniedHandler;
import net.ttcxy.chat.security.jwt.JwtAuthenticationEntryPoint;
import net.ttcxy.chat.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Value("${tang.openUrls}")
    private String[] filePath;


    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationErrorHandler;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * WebSecurity 配置级别跟高。
     */
    @Override
    public void configure(WebSecurity web) {
        // 过滤OPTIONS请求，减低无用拦截
        web.ignoring().antMatchers(HttpMethod.OPTIONS);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers(filePath).permitAll();
        JwtFilter jwtFilter = new JwtFilter(tokenProvider);
        httpSecurity
                .csrf().disable()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationErrorHandler)// 登录失败处理器
                .accessDeniedHandler(jwtAccessDeniedHandler)// 拒绝处理器

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and().logout().logoutUrl("/api/logout")

                // 关闭SESSION
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/authenticate","/api/register").permitAll()

                .antMatchers("/api/admin").hasAuthority("ROLE_ADMIN")

                .anyRequest().authenticated();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new FilterRegistrationBean<>(new CorsFilter(source));
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("mima135654.."));
    }
}
