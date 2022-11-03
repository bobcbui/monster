package net.ttcxy.chat.entity.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CtsRelationMember {

    @Id
    private String id;

    private String memberUrl;

    private String beMemberUrl;

    private boolean pass;

}
