package net.ttcxy.chat.entity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class ResultMap {

    private String type;

    private Object data;

    public ResultMap(String type, Object data){
        this.type = type;
        this.data = data;
    }

    /**
     * @return
     */
    public String toJSON(){
        Map<String,Object> obj = new HashMap<>();
        obj.put("type", type);
        obj.put("data", data);
        return JSONObject.toJSONString(obj);
    }
}
