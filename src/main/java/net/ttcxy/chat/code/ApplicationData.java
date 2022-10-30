package net.ttcxy.chat.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ttcxy.chat.entity.model.CtsMember;

import javax.websocket.Session;

public class ApplicationData {
    public final static Map<String, CtsMember> tokenMemberMap = new HashMap<>();

    public final static Map<Long, List<Session>> memberIdSessionMap = new HashMap<>();

    public final static Map<Long, List<Session>> groupIdSessionMap = new HashMap<>();

}
