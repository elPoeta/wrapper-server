package com.browxy.wrapper.response;

import com.browxy.wrapper.status.StatusMessageResponse;
import com.google.gson.Gson;

public class ResponseMessageUtil {

	public static String getStatusMessage(String message, int statusCode) {
		StatusMessageResponse messageResponse = StatusMessageResponse.getInstance();
		messageResponse.setStatusCode(statusCode);
		messageResponse.setMessage(message);
		return new Gson().toJson(messageResponse, StatusMessageResponse.class);
	}

	public static String getStatusMessage(String message) {
		StatusMessageResponse messageResponse = StatusMessageResponse.getInstance();
		messageResponse.setMessage(message);
		return new Gson().toJson(messageResponse, StatusMessageResponse.class);
	}
}
