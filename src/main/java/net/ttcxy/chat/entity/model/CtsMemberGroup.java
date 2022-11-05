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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long groupId;

    private String groupName;
}
