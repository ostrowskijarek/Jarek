package com.test.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTools {

	static ObjectMapper mapper = new ObjectMapper();

	public static Object toObject(String json, Class c) throws Exception {
		Object obj = mapper.readValue(json, c);
		return obj;
	}
}
