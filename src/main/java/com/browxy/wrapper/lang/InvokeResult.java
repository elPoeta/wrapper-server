package com.browxy.wrapper.lang;

public class InvokeResult {
	private String result;
	private int statusCode;
	private String message;

	public InvokeResult() {}
		
	public InvokeResult(String result, int statusCode, String message) {
		this.result = result;
		this.statusCode = statusCode;
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

	public boolean hasError() {
		return this.statusCode != 200;
	}
}
