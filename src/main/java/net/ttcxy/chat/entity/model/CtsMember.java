package net.ttcxy.chat.entity.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
public class CtsMember implements Serializable {

    @Id
    @GeneratedValue
    private String id;

    private String address;

    private String username;

    private String password;

    private Date createTime;

}