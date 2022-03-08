package com.bjpowernode.crm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

    private JsonUtil() {

    }

    public static void printJson(HttpServletResponse response, boolean flag) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);
        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(map);
            response.getWriter().print(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void printJson(HttpServletResponse response, String msg) {
        Map<String, Object> map = new HashMap();
        map.put("success", false);
        map.put("msg", msg);
        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(map);
            response.getWriter().print(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printJson(HttpServletResponse response, Object obj) {
        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(obj);
            response.getWriter().print(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
