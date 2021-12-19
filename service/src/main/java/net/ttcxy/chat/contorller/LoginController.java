package net.ttcxy.chat.contorller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.ttcxy.chat.api.ApiException;
import net.ttcxy.chat.api.ResponseCode;
import net.ttcxy.chat.api.ResponseResult;
import net.ttcxy.chat.entity.LoginParam;
import net.ttcxy.chat.entity.RegisterParam;
import net.ttcxy.chat.entity.model.Member;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = loginParam.getRememberMe() != null && loginParam.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        return new ResponseEntity<>(new JwtToken(jwt),HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseResult<?> register(@RequestBody RegisterParam param){
        String username = param.getUsername();
        Member member = BeanUtil.toBean(param, Member.class);
        String password = param.getPassword();
        member.setPassword(new BCryptPasswordEncoder().encode(password));
        member.setUsername(username);
        member.setUsernametwo("未设置用户名");
        member.setId(IdUtil.objectId());
        member.setCreateTime(new DateTime());
        int count = memberService.insertMember(member);
        if (count > 0){
            return ResponseResult.success("注册成功");
        }
        throw new ApiException(ResponseCode.FAILED);
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
