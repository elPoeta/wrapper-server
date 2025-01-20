package com.browxy.wrapper.error;

public class ErrorMessageResponse {
	private int statusCode;
	private String message;

	public ErrorMessageResponse() {
	}

	public ErrorMessageResponse(int statusCode) {
		this.statusCode = statusCode;
	}

	public static ErrorMessageResponse getInstance() {
		return new ErrorMessageResponse(400);
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
