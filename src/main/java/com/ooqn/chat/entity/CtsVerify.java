package com.ooqn.chat.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsVerify {
    
    @Id
    private String id;

    private String memberId;

    private String context;

    /**
     * 1：好友申请，2：群组申请
     */
    private String type;
    
    /**
     * 0：未处理，1：已处理
     */
    private Integer state;
    
    private Date createTime;

    private Date updateTime;
    
}
