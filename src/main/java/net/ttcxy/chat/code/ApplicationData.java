package net.ttcxy.chat.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ttcxy.chat.entity.model.CtsUser;

import jakarta.websocket.Session;

public class ApplicationData {

    public final static Map<String, CtsUser> tokenUserMap = new HashMap<>();

    public final static Map<String, CtsUser> tokenSocketMap = new HashMap<>();

    public final static Map<Long, List<Session>> userIdSessionMap = new HashMap<>();

    public final static Map<Long, List<Session>> groupIdSessionMap = new HashMap<>();

}
