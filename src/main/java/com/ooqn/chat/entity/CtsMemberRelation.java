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

    private String account;

    private String username;

    private String nickname;

    private String alias;

    /** 关系状态，0：已申请，1：申请通过，2：拒收消息 */
    private Integer state;

    private Date lastReadTime;
    
    private Date createTime;

    private Date updateTime;

    private String cause;

}
