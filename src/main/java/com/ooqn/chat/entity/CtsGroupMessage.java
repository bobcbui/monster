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

    private String account;

    private String acceptGroupId;

    private String content;

    private Date createTime;

}