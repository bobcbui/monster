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
    
    /** 0：未处理，1：你已通过 ,2: 你已拒绝, 3: 等待验证, 4：申请被通过，5: 申请被拒绝 */
    private Integer state;
    
    private Date createTime;

    private Date updateTime;
    
}
