package net.ttcxy.chat.entity.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsMessageMember {

    @Id
    private String id;

    private String receiveMemberId;

    private String memberId;

    private String text;

    private Date createTime;

}