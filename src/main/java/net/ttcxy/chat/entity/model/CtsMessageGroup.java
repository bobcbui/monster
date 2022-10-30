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
public class CtsMessageGroup {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long receiveGroupId;
    
    private Long memberId;

    private String text;

    private Date createTime;

}