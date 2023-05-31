package net.ttcxy.chat.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
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
     * 发送消息的用户 WS
     */
    private String sendMemberWs;

    /**
     * 接收消息的用户 Ws
     */
    private String acceptMemberWs;

    /**
     * 消息所属用户
     */
    private String memberId;

    /**
     * 消息内容HTML
     */
    private String content;

    private Date createTime;

}