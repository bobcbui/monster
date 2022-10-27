package net.ttcxy.chat.entity.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
public class CtsMessage {

    @Id
    @GeneratedValue
    private String id;

    private String memberId;

    // memberId , groupId
    private String subscribeId;

    private String html;

    private Date createTime;

}