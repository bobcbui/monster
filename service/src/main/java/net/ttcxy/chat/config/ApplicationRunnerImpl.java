package net.ttcxy.chat.config;

import net.ttcxy.chat.entity.model.Gang;
import net.ttcxy.chat.service.GangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private GangService gangService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Gang> gangs = gangService.selectAllGang();
        for (Gang gang : gangs) {
            ApplicationWebSocket.gangMap.put(gang.getId(),gang);
        }
    }
}