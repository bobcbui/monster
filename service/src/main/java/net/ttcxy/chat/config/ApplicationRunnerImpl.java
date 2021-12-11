package net.ttcxy.chat.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import net.ttcxy.chat.entity.model.Gang;
import net.ttcxy.chat.service.GangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

            JSONObject msg = new JSONObject();
            msg.put("to",gang.getId());
            msg.put("from","system");
            msg.put("fromName","系统消息");
            msg.put("username","系统消息");
            msg.put("createTime", DateUtil.date());
            msg.put("text","欢迎加入"+gang.getName());
            msg.put("id", IdUtil.objectId());
            msg.put("type",1);
            msg.put("gangName",gang.getName());
            msg.put("gangId",gang.getId());
            ArrayList<JSONObject> objects = new ArrayList<>();
            objects.add(msg);
            ApplicationWebSocket.gangMessage.put(gang.getId(),objects);
        }
    }
}