package net.ttcxy.chat.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Result {
    public static String r(String type,String code, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("type", type);
        jsonObject.put("data", data);
        return JSON.toJSONString(jsonObject, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String success = "200";
    public static String fail = "500";
    public static String error = "400";
    public static String notLogin = "401";
    public static String notFound = "404";
    public static String notAllow = "403";
    public static String notAllowMethod = "405";
    public static String notAllowMediaType = "406";
    public static String notAllowParam = "407";
}
