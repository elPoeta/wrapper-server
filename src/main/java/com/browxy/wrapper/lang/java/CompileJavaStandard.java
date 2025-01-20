package com.browxy.wrapper.lang.java;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.lang.CompilerCode;
import com.browxy.wrapper.message.JavaMessage;
import com.browxy.wrapper.message.Message;

public class CompileJavaStandard implements CompilerCode {
	private static final Logger logger = LoggerFactory.getLogger(CompileJavaStandard.class);

	@Override
	public boolean compileUserCode(Message message) {
		JavaMessage javaMessage = (JavaMessage) message;
		String containerBasePath = System.getProperty("containerBasePath");
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			logger.error("No Java compiler found.");
			return false;
		}
		
		String targetDirectory = containerBasePath + "/target/classes";
		String libDirectory = containerBasePath + "/libraries";
		String filePath = containerBasePath + "" + javaMessage.getUserCodePath(); 
		createDirectory(targetDirectory);
		
		String classpath = getClasspathFromLibDirectory(libDirectory);
		String[] compileOptions = new String[] { "-d", targetDirectory, "-classpath", classpath, filePath };

		int result = compiler.run(null, null, null, compileOptions);
		return result == 0;
	}

	private String getClasspathFromLibDirectory(String libDirectory) {
		List<String> jarFiles = new ArrayList<>();
		File libDir = new File(libDirectory);
		if (libDir.exists() && libDir.isDirectory()) {
			File[] files = libDir.listFiles((dir, name) -> name.endsWith(".jar"));

			if (files != null) {
				for (File file : files) {
					logger.info("add jar file {}", file.getAbsolutePath());
					jarFiles.add(file.getAbsolutePath());
				}
			}
		}

		return String.join(File.pathSeparator, jarFiles);
	}

	private void createDirectory(String targetDirectory) {
		File dir = new File(targetDirectory);
		if (dir.exists()) {
			deleteDirectoryAndContents(dir);
		}
		boolean success = dir.mkdirs();
		if (success) {
			logger.info("Target directory created: {}", targetDirectory);
		} else {
			logger.error("Failed to create target directory: {}", targetDirectory);
		}
	}

	private void deleteDirectoryAndContents(File dir) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDirectoryAndContents(file);
				} else {
					if (file.delete()) {
						logger.info("Deleted file: {}", file.getAbsolutePath());
					} else {
						logger.error("Failed to delete file: {}", file.getAbsolutePath());
					}
				}
			}
		}
		if (dir.delete()) {
			logger.info("Deleted directory: {}", dir.getAbsolutePath());
		} else {
			logger.error("Failed to delete directory: {}", dir.getAbsolutePath());
		}
	}
}
