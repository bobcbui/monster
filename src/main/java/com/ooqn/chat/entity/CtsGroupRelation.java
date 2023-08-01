package com.ooqn.chat.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 * 群里面所有的成员
 */
@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames= {"groupAccount", "memberAccount"}))
public class CtsGroupRelation implements Serializable {

    @Id
    private String id;

    private String groupName;

    private String nickname;

    private String alias;

    private String groupAccount;

    /** 成员角色 群主：1，管理员：2，普通成员：3 */
    private String memberRole;

    private String memberAccount;

    private String memberNickname;

    private Date readTime;

    private Date createTime;

}
