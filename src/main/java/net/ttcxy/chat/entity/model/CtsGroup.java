package net.ttcxy.chat.entity.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import net.ttcxy.chat.ChatApplication;

@Getter
@Setter
@Entity
public class CtsGroup implements Serializable {

    @Id
    @Column(name = "group_name")
    private String groupName;

    private String nickname;

    private String createUsername;

    private Date createTime;

    public String getWs(){
        return ChatApplication.host+"/group/"+groupName;
    }

}