package net.ttcxy.chat.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ResultMap {
    public static String result(String type,String serviceId, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serviceId", serviceId);
        jsonObject.put("type", type);
        jsonObject.put("data", data);
        return JSON.toJSONString(jsonObject, SerializerFeature.DisableCircularReferenceDetect);
    }
}
