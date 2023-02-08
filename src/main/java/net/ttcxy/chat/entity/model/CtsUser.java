package net.ttcxy.chat.entity.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsUser implements Serializable{

    @Id
    private String id;

    private String username;

    private String nickname;

    private String password;

    private Date createTime;

}