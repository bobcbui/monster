package net.ttcxy.chat.mapper;

import java.util.List;
import net.ttcxy.chat.entity.model.Message;
import net.ttcxy.chat.entity.model.MessageExample;
import org.apache.ibatis.annotations.Param;

public interface MessageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Thu Dec 02 21:32:15 CST 2021
     */
    long countByExample(MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Thu Dec 02 21:32:15 CST 2021
     */
    int deleteByExample(MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Thu Dec 02 21:32:15 CST 2021
     */
    int insert(Message record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Thu Dec 02 21:32:15 CST 2021
     */
    int insertSelective(Message record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Thu Dec 02 21:32:15 CST 2021
     */
    List<Message> selectByExample(MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Thu Dec 02 21:32:15 CST 2021
     */
    int updateByExampleSelective(@Param("record") Message record, @Param("example") MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Thu Dec 02 21:32:15 CST 2021
     */
    int updateByExample(@Param("record") Message record, @Param("example") MessageExample example);
}