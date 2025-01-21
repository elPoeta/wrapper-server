package com.browxy.wrapper.fileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManager {
	private static final Logger logger = LoggerFactory.getLogger(FileManager.class);
	
	public static String readFile(String completeFilePath) throws FileNotFoundException {
		return readFile(completeFilePath, "UTF-8");
	}

	public static String readFile(String fileName, String encoding) throws FileNotFoundException {
		return readFile(new FileInputStream(fileName), encoding);
	}

	public static String readFile(InputStream inputStream, String encoding) {
		return readFile(inputStream, encoding, true);
	}

	public static String readFile(InputStream inputStream, String encoding, boolean preserveCR) {
		StringBuilder strBuilder = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encoding))) {
			strBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				strBuilder.append(line + (preserveCR ? '\n' : ""));
			}
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		return strBuilder.toString();
	}

	public static void createDirectory(String targetDirectory) {
		File dir = new File(targetDirectory);
		if (!dir.exists()) {
			boolean success = dir.mkdirs();
			if (success) {
				logger.info("Target directory created: {}", targetDirectory);
			} else {
				logger.error("Failed to create target directory: {}", targetDirectory);
			}
		}
	}
}
