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
public class CtsGroupMember {

    @Id
    @GeneratedValue
    private String id;

    private String groupId;

    private String memberId;

    private Date createTime;

}