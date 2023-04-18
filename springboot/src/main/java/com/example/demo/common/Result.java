package com.example.demo.common;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class Result {

    public static String success(Object res) {
        Map<String,Object> map = new HashMap<>();
        map.put("result",1);
        map.put("data",res);
        return JSON.toJSONString(map);
    }
    public static String success() {
        Map<String,Object> map = new HashMap<>();
        map.put("result",1);
        return JSON.toJSONString(map);
    }

    public static String error() {
        Map<String,Object> map = new HashMap<>();
        map.put("result",0);
        return JSON.toJSONString(map);
    }

    //失败信息
    public static String errMsg(String msg){
        Map<String,Object> map = new HashMap<>();
        map.put("result",0);
        map.put("msg",msg);
        return JSON.toJSONString(map);
    }

    //成功信息
    public static String successMsg(String msg){
        Map<String,Object> map = new HashMap<>();
        map.put("result",1);
        map.put("msg",msg);
        return JSON.toJSONString(map);
    }
}
