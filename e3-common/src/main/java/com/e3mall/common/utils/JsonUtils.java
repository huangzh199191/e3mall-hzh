package com.e3mall.common.utils;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	//定义jackson对象
	private static final ObjectMapper mapper = new ObjectMapper(); 
	//将java对象转换为json字符串
	public static String ObjectToJson(Object data){
		try {  
            String json = mapper.writeValueAsString(data);  
            return json;  
        } catch (JsonProcessingException e) {  
            e.printStackTrace();  
        }
		return null;
	}
	//将json字符串转换成java对象
	public static <T> T jsonToObject(String jsonData, Class<T> objectType) {  
        try {  
            T t = mapper.readValue(jsonData, objectType);  
            return t;  
        }  catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
	//将json数据转换成pojo对象list 
	public static <T> List<T> jsonToObjectList(String jsonData, Class<T> objectType) {  
        try {  
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, objectType);  
            List<T> list = mapper.readValue(jsonData, javaType);  
            return list;  
        }  catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
	
}
