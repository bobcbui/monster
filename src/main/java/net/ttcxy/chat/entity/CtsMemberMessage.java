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

    private String sendMemberAccount;

    private String acceptMemberAccount;

    private String memberId;

    private String content;

    private Date createTime;

}