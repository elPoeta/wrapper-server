package com.browxy.wrapper.lang.java;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.lang.CompilerCode;
import com.browxy.wrapper.lang.CompilerResult;
import com.browxy.wrapper.lang.CustomClassLoader;
import com.browxy.wrapper.message.JavaMessage;
import com.browxy.wrapper.message.Message;

public class CompileJavaStandard implements CompilerCode {
	private static final Logger logger = LoggerFactory.getLogger(CompileJavaStandard.class);

	@Override
	public CompilerResult compileUserCode(Message message) {
		JavaMessage javaMessage = (JavaMessage) message;
		String containerBasePath = System.getProperty("containerBasePath");
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			logger.error("No Java compiler found.");
			return new CompilerResultJava(false, Collections.emptyList());
		}

		String targetDirectory = containerBasePath + File.separator + "target" + File.separator + "classes";
		String libDirectory = containerBasePath + File.separator + "libraries";
		String filePath = containerBasePath + "" + javaMessage.getUserCodePath();
		createDirectory(targetDirectory);

		List<String> dependencieJars = CustomClassLoader.getClasspathFromLibDirectory(libDirectory);
		String classpath = String.join(File.pathSeparator, dependencieJars);
		String[] compileOptions = new String[] { "-d", targetDirectory, "-classpath", classpath, filePath };

		int result = compiler.run(null, null, null, compileOptions);
		return new CompilerResultJava(result == 0, dependencieJars);
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
