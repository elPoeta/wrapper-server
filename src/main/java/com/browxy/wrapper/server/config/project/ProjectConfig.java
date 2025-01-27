package com.browxy.wrapper.server.config.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ProjectConfig {
	private int socketPort;
	private String entryPoint;
	private JsonObject properties;
	private JsonArray page;

	public int getSocketPort() {
		return socketPort;
	}

	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}

	public String getEntryPoint() {
		return entryPoint;
	}

	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}

	public JsonObject getProperties() {
		return properties;
	}

	public void setProperties(JsonObject properties) {
		this.properties = properties;
	}

	public JsonArray getPage() {
		return page;
	}

	public void setPage(JsonArray page) {
		this.page = page;
	}

}
