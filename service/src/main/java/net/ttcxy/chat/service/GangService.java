package net.ttcxy.chat.service;

import net.ttcxy.chat.entity.model.Gang;
import net.ttcxy.chat.entity.model.GangMember;
import net.ttcxy.chat.entity.model.GangMemberExample;
import net.ttcxy.chat.mapper.GangMapper;
import net.ttcxy.chat.mapper.GangMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GangService {


    private final GangMapper groupMapper;

    private final GangMemberMapper groupMemberMapper;

    @Autowired
    public GangService(GangMapper groupMapper, GangMemberMapper groupMemberMapper) {
        this.groupMapper = groupMapper;
        this.groupMemberMapper = groupMemberMapper;
    }

    /**
     * 查询所有的群列表
     */
    public List<Gang> selectAllGang() {
        return groupMapper.selectByExample(null);
    }

    /**
     * 通过用户ID查询成员加入的群
     * @param memberId 成员ID
     */
    public List<GangMember> selectMemberGang(String memberId){
        GangMemberExample gme = new GangMemberExample();
        gme.createCriteria().andMemberIdEqualTo(memberId);
        return groupMemberMapper.selectByExample(gme);
    }
}
