package com.sogou.bizwork.task.api.common.util;

import net.sf.json.JSONArray;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * JSON与Object转换工具
 * 
 */
public class JSONUtils {

	static ObjectMapper mapper = new ObjectMapper();

	public static String serializeObject(Object o) throws Exception {
		return mapper.writeValueAsString(o);
	}

	public static Object deserializeObject(String s, Class<?> clazz) throws Exception {
		return mapper.readValue(s, clazz);
	}

	public static Object deserializeObject(String s, TypeReference<?> typeReference)
	        throws Exception {
		return mapper.readValue(s, typeReference);
	}

	public static String toJsonString(Object data) {
		if (data == null) {
			return "[]";
		}
		return JSONArray.fromObject(
		        data instanceof String ? new String[] { data.toString() } : data).toString();
	}
	
	public static <T> T deserializeObjectGenerics(String s, Class<T> clazz) throws Exception {
		return mapper.readValue(s, clazz);
	}

}
