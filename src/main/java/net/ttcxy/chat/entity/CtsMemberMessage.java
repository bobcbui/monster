package net.ttcxy.chat.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * 别人发送给用户的消息
 */
@Getter
@Setter
@Entity
public class CtsMemberMessage implements Serializable {

    @Id
    private String id;

    /**
     * 发送消息的用户ID
     */
    private String sendMemberId;

    /**
     * 接收消息的用户ID
     */
    private String acceptMemberId;

    /**
     * 消息内容HTML
     */
    private String content;

    private Date createTime;

}