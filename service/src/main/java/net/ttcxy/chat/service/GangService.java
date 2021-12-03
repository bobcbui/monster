package net.ttcxy.chat.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import net.ttcxy.chat.config.ApplicationWebSocket;
import net.ttcxy.chat.dao.MemberDao;
import net.ttcxy.chat.entity.model.Gang;
import net.ttcxy.chat.entity.model.GangMember;
import net.ttcxy.chat.entity.model.GangMemberExample;
import net.ttcxy.chat.mapper.GangMapper;
import net.ttcxy.chat.mapper.GangMemberMapper;
import net.ttcxy.chat.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * 添加成员到当前gang
     */
    public int insertGangMember(String gangId, String memberId) {
        GangMember gangMember = new GangMember();
        gangMember.setId(IdUtil.objectId());
        gangMember.setGangId(gangId);
        gangMember.setMemberId(memberId);
        gangMember.setCreateTime(DateUtil.date());

        Set<String> strings = ApplicationWebSocket.gangMember.get(gangId);
        if (strings == null){
            strings = new HashSet<>();
            strings.add(memberId);
            ApplicationWebSocket.gangMember.put(gangId,strings);
        }else{
            strings.add(memberId);
        }
        return groupMemberMapper.insert(gangMember);
    }


}
