package com.browxy.wrapper.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesReader {
	private static final Logger logger = LoggerFactory.getLogger(PropertiesReader.class);

	private static Config config;

	public static Config read(String resource) {
		Properties properties = new Properties();

		try (InputStream inputStream = PropertiesReader.class.getClassLoader()
				.getResourceAsStream(resource)) {
			if (inputStream != null) {
				properties.load(inputStream);
				config = new Config(properties);

			} else {
				logger.error("Properties file not found!");
			}
		} catch (IOException e) {
			logger.error("error reading properties file", e);
		}
		return config;
	}
}
