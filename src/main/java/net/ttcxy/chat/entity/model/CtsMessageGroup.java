package net.ttcxy.chat.entity.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsMessageGroup {

    @Id
    private String id;

    private String receiveGroupId;
    
    private String memberId;

    private String text;

    private Date createTime;

}