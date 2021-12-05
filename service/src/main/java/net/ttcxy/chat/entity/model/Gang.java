package net.ttcxy.chat.entity.model;

import java.io.Serializable;
import java.util.Date;

public class Gang implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gang.id
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gang.name
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column gang.create_time
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table gang
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gang.id
     *
     * @return the value of gang.id
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gang.id
     *
     * @param id the value for gang.id
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gang.name
     *
     * @return the value of gang.name
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gang.name
     *
     * @param name the value for gang.name
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column gang.create_time
     *
     * @return the value of gang.create_time
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column gang.create_time
     *
     * @param createTime the value for gang.create_time
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gang
     *
     * @mbg.generated Sun Dec 05 11:21:04 GMT+08:00 2021
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}