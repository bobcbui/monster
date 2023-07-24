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

    private String data;

    /** 1：好友申请，2：群组申请 */
    private String type;
    
    /** 0：未处理，1：已通过 ,2: 等待通过, 3: 被通过 */
    private Integer state;
    
    private Date createTime;

    private Date updateTime;
    
}
