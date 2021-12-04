package net.ttcxy.chat.entity.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.id
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.from
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    private String from;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.to
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    private String to;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.text
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    private String text;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.type
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.create_time
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table message
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.id
     *
     * @return the value of message.id
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.id
     *
     * @param id the value for message.id
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.from
     *
     * @return the value of message.from
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public String getFrom() {
        return from;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.from
     *
     * @param from the value for message.from
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.to
     *
     * @return the value of message.to
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public String getTo() {
        return to;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.to
     *
     * @param to the value for message.to
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.text
     *
     * @return the value of message.text
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public String getText() {
        return text;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.text
     *
     * @param text the value for message.text
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.type
     *
     * @return the value of message.type
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.type
     *
     * @param type the value for message.type
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.create_time
     *
     * @return the value of message.create_time
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.create_time
     *
     * @param createTime the value for message.create_time
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Sun Dec 05 00:32:44 GMT+08:00 2021
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", from=").append(from);
        sb.append(", to=").append(to);
        sb.append(", text=").append(text);
        sb.append(", type=").append(type);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}