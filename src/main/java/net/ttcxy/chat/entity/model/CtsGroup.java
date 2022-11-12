package net.ttcxy.chat.entity.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsGroup {

    @Id
    private String groupName;

    private String nickname;

    private String createUsername;

    private Date createTime;

}