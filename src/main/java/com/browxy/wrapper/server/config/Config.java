package com.browxy.wrapper.server.config;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Config {
	private static final Logger logger = LoggerFactory.getLogger(Config.class);
	
	private static Config instance = null;
	private static final Object lock = new Object();

	private Map<String, String> configValues;

	private Config() {
		Properties properties = getProperties();
		configValues = new HashMap<>();

		configValues.put("server.port", properties.getProperty("server.port"));
		configValues.put("server.staticDir", System.getenv("STATIC_DIR") != null ? System.getenv("STATIC_DIR")
				: properties.getProperty("server.staticDir"));
		configValues.put("server.staticFile", System.getenv("STATIC_FILE") != null ? System.getenv("STATIC_FILE")
				: properties.getProperty("server.staticFile"));
		configValues.put("server.entryPoint", System.getenv("ENTRY_POINT") != null ? System.getenv("ENTRY_POINT")
				: properties.getProperty("server.entryPoint"));
		configValues.put("server.storage", properties.getProperty("server.storage"));
		configValues.put("datasource.ip", System.getenv("DATASOURCE_IP") != null ? System.getenv("DATASOURCE_IP") : "");
		configValues.put("datasource.port",
				System.getenv("DATASOURCE_PORT") != null ? System.getenv("DATASOURCE_PORT") : "");
		configValues.put("datasource.dbname",
				System.getenv("DATASOURCE_DB_NAME") != null ? System.getenv("DATASOURCE_DB_NAME") : "");
		configValues.put("datasource.username",
				System.getenv("DATASOURCE_USER_NAME") != null ? System.getenv("DATASOURCE_USER_NAME") : "");
		configValues.put("datasource.password",
				System.getenv("DATASOURCE_PASSWORD") != null ? System.getenv("DATASOURCE_PASSWORD") : "");
	}

	public static Config getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new Config();
				}
			}
		}
		return instance;
	}

	private static Properties getProperties() {
		String resource = System.getProperty("dev") == null ? "resource.server.properties"
				: "resource.server.dev.properties";
		Properties properties = new Properties();

		try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(resource)) {
			if (inputStream != null) {
				properties.load(inputStream);

			} else {
				logger.error("Properties file not found!");
			}
		} catch (IOException e) {
			logger.error("error reading properties file", e);
		}
		return properties;
	}

	public String get(String key) {
		return configValues.get(key);
	}

	public void set(String key, String value) {
		configValues.put(key, value);
	}

	public int getPort() {
		return Integer.parseInt(configValues.get("server.port"));
	}

	public void setPort(int port) {
		configValues.put("server.port", String.valueOf(port));
	}

	public String getStaticDir() {
		return configValues.get("server.staticDir");
	}

	public void setStaticDir(String staticDir) {
		configValues.put("server.staticDir", staticDir);
	}

	public String getStaticFile() {
		return configValues.get("server.staticFile");
	}

	public void setStaticFile(String staticFile) {
		configValues.put("server.staticFile", staticFile);
	}

	public String getEntryPoint() {
		return configValues.get("server.entryPoint");
	}

	public void setEntryPoint(String entryPoint) {
		configValues.put("server.entryPoint", entryPoint);
	}

	public String getStorage() {
		return configValues.get("server.storage");
	}

	public void setStorage(String storage) {
		configValues.put("server.storage", storage);
	}

	public String getDataSourceIp() {
		return configValues.get("datasource.ip");
	}

	public void setDataSourceIp(String ip) {
		configValues.put("datasource.ip", ip);
	}

	public int getDataSourcePort() {
		return Integer.parseInt(configValues.get("datasource.port"));
	}

	public void setDataSourcePort(int port) {
		configValues.put("datasource.port", String.valueOf(port));
	}

	public String getDataSourceDbName() {
		return configValues.get("datasource.dbname");
	}

	public void setDataSourceDbName(String dbname) {
		configValues.put("datasource.dbname", dbname);
	}

	public String getDataSourceUserName() {
		return configValues.get("datasource.username");
	}

	public void setDataSourceUserName(String username) {
		configValues.put("datasource.username", username);
	}

	public String getDataSourcePassword() {
		return configValues.get("datasource.password");
	}

	public void setDataSourcePassword(String password) {
		configValues.put("datasource.password", password);
	}

	public String getDataSourceUrl(String connector, String encoding) {
		return connector + "://" + getDataSourceIp() + "/" + getDataSourceDbName() + "?characterEncoding=" + encoding;
	
	}		
}
