package net.ttcxy.chat.code;

import com.alibaba.fastjson.JSONObject;

public class ResultMap {
    public static String result(String type, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("data", data);
        return jsonObject.toJSONString();
    }
}
