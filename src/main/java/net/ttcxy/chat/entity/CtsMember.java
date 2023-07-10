package net.ttcxy.chat.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import net.ttcxy.chat.util.SpringUtil;

/**
 * 注册用户
 */
@Getter
@Setter
@Entity
// username 唯一 不能重复
@Table(uniqueConstraints = @UniqueConstraint(columnNames= {"username"}))
public class CtsMember implements Serializable{

    @Id
    @JsonIgnore
    private String id;

    private String username;

    private String nickname;

    @JsonIgnore
    private String password;

    private Date createTime;

    public String getAccount(){
        String prefix = SpringUtil.getApplicationContext().getEnvironment().getProperty("chat.member-prefix");
        String domain = SpringUtil.getApplicationContext().getEnvironment().getProperty("chat.domain");        
        return "@"+prefix+"."+username+"@"+domain;
    }

}