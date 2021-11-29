package net.ttcxy.chat.service;

import net.ttcxy.chat.entity.model.Group;
import net.ttcxy.chat.entity.model.GroupMember;
import net.ttcxy.chat.entity.model.GroupMemberExample;
import net.ttcxy.chat.mapper.GroupMapper;
import net.ttcxy.chat.mapper.GroupMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {


    private final GroupMapper groupMapper;

    private final GroupMemberMapper groupMemberMapper;

    @Autowired
    public GroupService(GroupMapper groupMapper, GroupMemberMapper groupMemberMapper) {
        this.groupMapper = groupMapper;
        this.groupMemberMapper = groupMemberMapper;
    }

    /**
     * 查询所有的群列表
     */
    public List<Group> selectAllGroup() {
        return groupMapper.selectByExample(null);
    }

    /**
     * 通过用户ID查询成员加入的群
     * @param memberId 成员ID
     */
    public List<GroupMember> selectMemberGroup(String memberId){
        GroupMemberExample gme = new GroupMemberExample();
        gme.createCriteria().andMemberIdEqualTo(memberId);
        return groupMemberMapper.selectByExample(gme);
    }
}
