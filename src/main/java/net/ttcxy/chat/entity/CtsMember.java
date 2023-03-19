package net.ttcxy.chat.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 注册用户
 */
@Getter
@Setter
@Entity
public class CtsMember implements Serializable{

    @Id
    private String id;

    /**
     * 6~15位英文数字组合，不可变更
     */
    private String name;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Date createTime;

}