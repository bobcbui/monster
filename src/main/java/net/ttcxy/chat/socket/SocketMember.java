package net.ttcxy.chat.socket;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import net.ttcxy.chat.code.ApplicationData;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.List;

@ServerEndpoint(value = "/socket/member")
@Component
public class SocketMember {

    @SneakyThrows
    @OnOpen
    public void onOpen(Session session) {
        String token = session.getPathParameters().get("token");
        String memberUrl = session.getPathParameters().get("memberUrl");
        String body = HttpUtil.get(memberUrl + "?memberUrl=" + memberUrl);
        if (!"true".equals(body)){
            session.getAsyncRemote().sendText("token 无效");
            session.close();
            return;
        }
        System.out.println("连接成功："+token);
    }

    @OnClose
    public void onClose() {
       
    }

    @OnMessage
    public void onMessage(String message, Session session) {

        JSONObject object = JSONObject.parseObject(message);
        String messageType = object.getString("type");
        String text = object.getString("text");
        String toUrl = object.getString("toUrl");

        JSONObject request = new JSONObject();
        request.put("toUrl",toUrl);
        request.put("text",text);
        request.put("time",new Date().getTime());
        request.put("fromUrl",session.getPathParameters().get("memberUrl"));

        if ("group".equals(messageType)){
            request.put("type","group");
        }

        if ("member".equals(messageType)){
            request.put("type","member");
        }

        List<Session> sessions = ApplicationData.toUrlSession.get(toUrl);
        for (Session se : sessions) {
            se.getAsyncRemote().sendText(request.toJSONString());
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
  
}
