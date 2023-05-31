package net.ttcxy.chat.entity;

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
@Table(uniqueConstraints = @UniqueConstraint(columnNames= {"memberId", "ws"}))
public class CtsMemberRelation implements Serializable {

    @Id
    private String id;

    /**
     * 发起关注的成员Id
     */
    private String memberId;

    /**
     * 被关注者的WS
     */
    private String ws;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 别名
     */
    private String alias;

    /**
     * 关系状态，0：被成员关注者，1：互相关注，2：拒绝接收消息
     */
    private Integer state;
    
    /**
     * 关系创建时间
     */
    private Date createTime;

}
