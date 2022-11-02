package net.ttcxy.chat.entity.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * 加入了哪些群
 */
@Getter
@Setter
@Entity
public class CtsMemberGroup {
    
    @Id
    private String id;

    private String memberId;

    private String groupId;

    private String groupName;
}
