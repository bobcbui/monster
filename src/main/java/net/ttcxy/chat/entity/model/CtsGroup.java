package net.ttcxy.chat.entity.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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