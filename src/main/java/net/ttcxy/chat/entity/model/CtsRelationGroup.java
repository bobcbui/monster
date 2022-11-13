package net.ttcxy.chat.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsRelationGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    private String ws;

    private Boolean pass;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_name",referencedColumnName = "group_name",insertable = false,updatable = false)
    private CtsGroup group;

}
