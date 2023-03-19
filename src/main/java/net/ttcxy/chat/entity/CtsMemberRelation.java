package net.ttcxy.chat.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames= {"userId", "beUserId"}))
public class CtsMemberRelation implements Serializable {

    @Id
    private Long id;

    /**
     * 发起关注的成员Id
     */
    private String memberId;

    /**
     * 对被关注者的别名
     */
    private String anotherName;

    /**
     * 被关注者的WS
     */
    private String ws;

    /**
     * 关系状态，0：被成员关注者，1：互相关注，2：拒绝接收消息
     */
    private Integer state;
    
    /**
     * 关系创建时间
     */
    private Date createTime;

}
