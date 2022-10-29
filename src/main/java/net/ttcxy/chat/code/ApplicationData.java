package net.ttcxy.chat.code;

import java.util.HashMap;
import java.util.Map;

import net.ttcxy.chat.entity.model.CtsMember;

public class ApplicationData {
    public final static Map<String, CtsMember> memberMap = new HashMap<>();
    public final static Map<String, CtsMember> messageTokenMap = new HashMap<>();
    public final static Map<String, String> tokenMemberUrl = new HashMap<>();
    
}
