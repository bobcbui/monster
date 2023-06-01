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
@Table(uniqueConstraints = @UniqueConstraint(columnNames= {"groupId", "memberId"}))
public class CtsGroupRelation implements Serializable {

    @Id
    private String id;

    /**
     * 群ID
     */
    private String groupId;

    /**
     * 群成员ID
     */
    private String memberId;

    /**
     * 成员角色 群主：1，管理员：2，普通成员：3
     */
    private String memberRole;

    
    /**
     * 成员WebSocket URL
     */
    private String memberWs;

    /**
     * 成员用户名
     */
    private String memberUsername;

    /**
     * 成员昵称
     */
    private String memberNickname;

    /**
     * 创建时间
     */
    private Date createTime;


}
