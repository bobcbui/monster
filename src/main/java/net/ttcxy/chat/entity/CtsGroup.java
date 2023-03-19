package net.ttcxy.chat.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 群
 */
@Getter
@Setter
@Entity
public class CtsGroup implements Serializable {

    @Id
    private String id;

    /**
     * 6~15位英文数字组合，不可变更
     */
    private String name;

    /**
     * 群昵称
     */
    private String nickname;

    /**
     * 简介
     */
    private String synopsis;

    /**
     * 创建人ID
     */
    private String createMemberId;

    /**
     * 创建时间
     */
    private Date createTime;

}