package net.ttcxy.chat.entity.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsSubscribe {
    
    @Id
    @GeneratedValue
    private String id;

    private String subscribeId;

    private String memberId;

    private Date createTime;
}
