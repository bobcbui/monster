package net.ttcxy.chat.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ttcxy.chat.entity.model.CtsMember;

import javax.websocket.Session;

public class ApplicationData {
    public final static Map<String, CtsMember> memberMap = new HashMap<>();

    public final static Map<String, CtsMember> messageTokenMap = new HashMap<>();
    public final static Map<String, String> tokenMemberUrl = new HashMap<>();

    public final static Map<String,String> checkToken = new HashMap<>();

    public final static Map<String, List<Session>> toUrlSession = new HashMap<>();


}
