package com.test.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTools {

	static ObjectMapper mapper = new ObjectMapper();

	public static <T> Object toObject(String json, Class<T> c) throws Exception {
		Object obj = mapper.readValue(json, c);
		return obj;
	}
}
