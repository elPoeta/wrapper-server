package com.browxy.wrapper.status;

public class StatusMessageResponse {
	private int statusCode;
	private String message;
    
	public StatusMessageResponse() {
	}

	public StatusMessageResponse(int statusCode) {
		this.statusCode = statusCode;
	}

	public static StatusMessageResponse getInstance() {
		return new StatusMessageResponse(400);
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
