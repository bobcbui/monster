package net.ttcxy.chat.entity;

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

    /**
     * 名称
     */
    private String groupName;

    /**
     * 群昵称
     */
    private String nickname;

    /**
     * 群别名
     */
    private String alias;

    /**
     * groupAccount
     */
    private String groupAccount;

    /**
     * 成员角色 群主：1，管理员：2，普通成员：3
     */
    private String memberRole;

    
    /**
     * 成员账户
     */
    private String memberAccount;

    /**
     * 成员昵称
     */
    private String memberNickname;

    /**
     * 创建时间
     */
    private Date createTime;


}
