package net.ttcxy.chat;

import net.ttcxy.chat.service.GroupService;
import net.ttcxy.chat.service.TestService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class ChatApplicationTests {

    @Autowired
    TestService testService;

    @Autowired
    GroupService groupService;

    @Test
    void contextLoads() {
        groupService.selectMemberGroup("1");
    }

}
