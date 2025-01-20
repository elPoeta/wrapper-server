package com.browxy.wrapper.message;

import com.google.gson.Gson;

public class MessageFactory {
	public static Message createMessage(String json) {
		String language = System.getProperty("language");
        Gson gson = new Gson();
		switch (language) {
		case "java":
			return gson.fromJson(json, JavaMessage.class);
		default:
			throw new IllegalArgumentException("Unknown language type: " + language);
		}
	}
}
