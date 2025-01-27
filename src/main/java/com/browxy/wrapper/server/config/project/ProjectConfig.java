package com.browxy.wrapper.server.config.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ProjectConfig {
	private int socketPort;
	private String entryPoint;
	private JsonObject properties;
	private JsonArray pages;

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

	public JsonArray getPages() {
		return pages;
	}

	public void setPage(JsonArray pages) {
		this.pages = pages;
	}

}
