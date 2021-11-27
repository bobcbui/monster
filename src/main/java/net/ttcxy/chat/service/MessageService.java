package net.ttcxy.chat.service;

import net.ttcxy.chat.db.tables.pojos.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MemberService memberService;
    private final GroupService groupService;

    @Autowired
    public MessageService(MemberService memberService, GroupService groupService){
        this.memberService = memberService;
        this.groupService = groupService;
    }

    // 获取一个这个用户上一次关闭连接的时间到当前时间点的所有消息
    public List<Message> selectMessage(String memberId,String closeTime) {
        List<String> strings = groupService.selectMemberGroup(memberId);
        for (String groupId : strings) {

        }
        return null;
    }

    // 发送消息给指定用户/群

}
