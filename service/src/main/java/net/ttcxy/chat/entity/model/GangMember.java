package net.ttcxy.chat.entity.model;

import java.io.Serializable;
import java.util.Date;

public class GangMember implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gang_member.id
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gang_member.gang_id
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    private String gangId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gang_member.member_id
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    private String memberId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gang_member.create_time
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table gang_member
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gang_member.id
     *
     * @return the value of gang_member.id
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gang_member.id
     *
     * @param id the value for gang_member.id
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gang_member.gang_id
     *
     * @return the value of gang_member.gang_id
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    public String getGangId() {
        return gangId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gang_member.gang_id
     *
     * @param gangId the value for gang_member.gang_id
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    public void setGangId(String gangId) {
        this.gangId = gangId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gang_member.member_id
     *
     * @return the value of gang_member.member_id
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gang_member.member_id
     *
     * @param memberId the value for gang_member.member_id
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gang_member.create_time
     *
     * @return the value of gang_member.create_time
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gang_member.create_time
     *
     * @param createTime the value for gang_member.create_time
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gang_member
     *
     * @mbg.generated Fri Dec 03 10:26:59 GMT+08:00 2021
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", gangId=").append(gangId);
        sb.append(", memberId=").append(memberId);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}