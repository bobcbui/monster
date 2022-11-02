package net.ttcxy.chat.entity.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

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