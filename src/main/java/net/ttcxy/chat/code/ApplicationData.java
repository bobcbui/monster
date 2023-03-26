package net.ttcxy.chat.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.websocket.Session;
import net.ttcxy.chat.entity.CtsMember;

public class ApplicationData {

    public final static Map<String, CtsMember> tokenMemberMap = new HashMap<>();

    public final static Map<String, CtsMember> tokenSocketMap = new HashMap<>();

    public final static Map<Long, List<Session>> memberIdSessionMap = new HashMap<>();

    public final static Map<Long, List<Session>> groupIdSessionMap = new HashMap<>();

}
