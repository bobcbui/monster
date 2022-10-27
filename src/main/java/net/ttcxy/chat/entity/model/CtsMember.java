package net.ttcxy.chat.entity.model;

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
public class CtsMember{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String address;

    private String username;

    private String password;

    private Date createTime;

}