package com.ooqn.chat.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 群消息
 */
@Getter
@Setter
@Entity
public class CtsGroupMessage implements Serializable {

    @Id
    private String id;

    /**
     * 消息发送人
     */
    private String account;

    /**
     * 接收这个消息的群
     */
    private String acceptGroupId;

    /**
     * 消息内容，HTML
     */
    private String content;

    /**
     * 消息插件时间
     */
    private Date createTime;

}