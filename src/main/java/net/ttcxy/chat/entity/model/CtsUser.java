package net.ttcxy.chat.entity.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import net.ttcxy.chat.ChatApplication;

@Getter
@Setter
@Entity
public class CtsUser implements Serializable{

    @Id
    private String username;

    private String password;

    private Date createTime;

    public String getWs(){
        return ChatApplication.host +"/"+this.username;
    }

}