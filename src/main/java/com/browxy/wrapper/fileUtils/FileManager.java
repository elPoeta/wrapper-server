package com.browxy.wrapper.fileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

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

	public static void setPermissions(String path) {
		try {
			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
				Path folderPath = Paths.get(path);
				Set<PosixFilePermission> perms = new HashSet<>();
				perms.add(PosixFilePermission.OWNER_READ);
				perms.add(PosixFilePermission.OWNER_WRITE);
				perms.add(PosixFilePermission.OWNER_EXECUTE);
				perms.add(PosixFilePermission.GROUP_READ);
				perms.add(PosixFilePermission.GROUP_WRITE);
				perms.add(PosixFilePermission.GROUP_EXECUTE);
				perms.add(PosixFilePermission.OTHERS_READ);
				perms.add(PosixFilePermission.OTHERS_WRITE);
				// perms.add(PosixFilePermission.OTHERS_EXECUTE);
				Files.setPosixFilePermissions(folderPath, perms);
			}
		} catch (IOException e) {
			logger.error("error setting permissions", e);
		}
	}
}
