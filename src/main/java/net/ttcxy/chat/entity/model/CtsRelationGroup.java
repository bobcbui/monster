package net.ttcxy.chat.entity.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsRelationGroup {

    @Id
    private String id;

    private String memberUrl;

    private String beGroupUrl;

    private Boolean pass;

}
