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

    /*谁的消息 */
    private String account;

    /*和谁的消息 */
    private String withAccount;

    /**发送消息的人 */
    private String sendAccount;

    /**接收消息的人 */
    private String acceptAccount;
    
    private String orderId;

    private String content;

    private Date createTime;

}