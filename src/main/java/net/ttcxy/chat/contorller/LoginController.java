package net.ttcxy.chat.contorller;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.ttcxy.chat.api.ApiException;
import net.ttcxy.chat.api.ResponseCode;
import net.ttcxy.chat.api.ResponseResult;
import net.ttcxy.chat.db.tables.pojos.Member;
import net.ttcxy.chat.entity.LoginParam;
import net.ttcxy.chat.entity.RegisterParam;
import net.ttcxy.chat.security.jwt.TokenProvider;
import net.ttcxy.chat.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final TokenProvider tokenProvider;

    private final MemberService memberService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public LoginController(TokenProvider tokenProvider,
                           MemberService memberService,
                           AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtToken> authorize(@Valid @RequestBody LoginParam loginParam) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginParam.getMail(), loginParam.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = loginParam.getRememberMe() != null && loginParam.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        return new ResponseEntity<>(new JwtToken(jwt),HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseResult<?> register(@RequestBody RegisterParam param){
        String username = param.getUsername();
        if (Validator.isEmail(username)){
            Member member = BeanUtil.toBean(param, Member.class);
            String password = param.getPassword();
            member.setPassword(new BCryptPasswordEncoder().encode(password));
            member.setUsername(username);
            int count = memberService.insertMember(member);
            if (count > 0){
                return ResponseResult.success("注册成功");
            }
            throw new ApiException(ResponseCode.FAILED);
        }else{
            throw new ApiException("请输入邮箱号");
        }
    }

    /**
     * JWT Authentication.
     */
    static class JwtToken {

        private String token;

        JwtToken(String token) {
            this.token = token;
        }

        @JsonProperty("token")
        String getToken() {
            return token;
        }

        void setToken(String token) {
            this.token = token;
        }
    }
}
