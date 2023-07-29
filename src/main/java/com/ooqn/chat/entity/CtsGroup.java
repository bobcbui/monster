package com.ooqn.chat.entity;

import java.io.Serializable;
import java.util.Date;

import com.ooqn.chat.util.SpringUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsGroup implements Serializable {

    @Id
    private String id;

    private String name;

    private String nickname;

    private String synopsis;

    private String createMemberId;

    // 1:公开 2:验证 3:正确答案 4:拒绝任何人 
    private String verifyType;

    private String verifyQuestion;

    private String verifyValue;

    private Date createTime;

    public String getAccount(){
        String prefix = SpringUtil.getApplicationContext().getEnvironment().getProperty("chat.group-prefix");
        String domain = SpringUtil.getApplicationContext().getEnvironment().getProperty("chat.domain");
        return "@"+prefix+"."+name+"@"+domain;
    }

}