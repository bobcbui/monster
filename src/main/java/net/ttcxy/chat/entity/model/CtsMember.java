package net.ttcxy.chat.entity.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsMember implements Serializable{

    @Id
    private String id;

    private String username;

    private String nickname;

    private String password;

    private Date createTime;

}