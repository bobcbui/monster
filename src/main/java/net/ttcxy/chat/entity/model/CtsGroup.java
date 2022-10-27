package net.ttcxy.chat.entity.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
public class CtsGroup {

    @Id
    @GeneratedValue
    private String id;

    private String name;

    private Date createTime;

}