package net.ttcxy.chat.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * member
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {
    private String id;

    private String username;

    private String password;

    private Date createTime;

    /**
     * 0是女  1是男
     */
    private Integer gender;

    /**
     * 里面的名字
     */
    private String usernametwo;

    /**
     * 是否可以私信
     */
    private Boolean letters;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 头像url
     */
    private String avatar;

    private static final long serialVersionUID = 1L;
}