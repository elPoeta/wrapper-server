package com.browxy.wrapper.response;

public class ResponseBuilder {
	private String payload;
	private String type;

	public ResponseBuilder(String payload, String type) {
		this.payload = payload;
		this.type = type;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
