package com.ooqn.chat.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户关注的好友
 */
@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames= {"memberId", "account"}))
public class CtsMemberRelation implements Serializable {

    @Id
    private String id;

    private String memberId;

    /**
     * 被关注者账户
     */
    private String account;

    private String username;

    private String nickname;

    private String alias;

    /**
     * 关系状态，0：被成员关注者，1：互相关注，2：拒绝接收消息
     */
    private Integer state;
    
    private Date createTime;

}
