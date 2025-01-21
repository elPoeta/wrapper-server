package com.browxy.wrapper.server.config;

import java.util.Properties;

public class Config {
	private int port;
	private String staticDir;
	private String staticFile;
	private String storage;
	private String builderPages;

	public Config(Properties properties) {
		this.port = Integer.parseInt(properties.getProperty("server.port"));
		this.staticDir = properties.getProperty("server.staticDir");
		this.staticFile = properties.getProperty("server.staticFile");
		this.storage = properties.getProperty("server.storage");
		this.builderPages = properties.getProperty("server.builderPages");
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getStaticDir() {
		return staticDir;
	}

	public void setStaticDir(String staticDir) {
		this.staticDir = staticDir;
	}

	public String getStaticFile() {
		return staticFile;
	}

	public void setStaticFile(String staticFile) {
		this.staticFile = staticFile;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getBuilderPages() {
		return builderPages;
	}

	public void setBuilderPages(String builderPages) {
		this.builderPages = builderPages;
	}

	
}
