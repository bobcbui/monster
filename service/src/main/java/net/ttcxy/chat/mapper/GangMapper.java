package net.ttcxy.chat.mapper;

import java.util.List;
import net.ttcxy.chat.entity.model.Gang;
import net.ttcxy.chat.entity.model.GangExample;
import org.apache.ibatis.annotations.Param;

public interface GangMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gang
     *
     * @mbg.generated Wed Dec 01 20:25:35 CST 2021
     */
    long countByExample(GangExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gang
     *
     * @mbg.generated Wed Dec 01 20:25:35 CST 2021
     */
    int deleteByExample(GangExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gang
     *
     * @mbg.generated Wed Dec 01 20:25:35 CST 2021
     */
    int insert(Gang record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gang
     *
     * @mbg.generated Wed Dec 01 20:25:35 CST 2021
     */
    int insertSelective(Gang record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gang
     *
     * @mbg.generated Wed Dec 01 20:25:35 CST 2021
     */
    List<Gang> selectByExample(GangExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gang
     *
     * @mbg.generated Wed Dec 01 20:25:35 CST 2021
     */
    int updateByExampleSelective(@Param("record") Gang record, @Param("example") GangExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gang
     *
     * @mbg.generated Wed Dec 01 20:25:35 CST 2021
     */
    int updateByExample(@Param("record") Gang record, @Param("example") GangExample example);
}