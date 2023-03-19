package net.ttcxy.chat.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class CtsResultData {

    private String type;

    private Object data;

    private Date dateTime;

    public CtsResultData(String type, Object data, Date dateTime){
        this.type = type;
        this.data = data;
        this.dateTime = dateTime;
    }

    /**
     * @return
     */
    public String toJSON(){
        Map<String,Object> obj = new HashMap<>();
        obj.put("type", type);
        obj.put("data", data);
        obj.put("dateTime", dateTime);
        return JSONObject.toJSONString(obj);
    }
}
