package com.browxy.wrapper.lang.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.lang.CompilerCode;
import com.browxy.wrapper.lang.CompilerResult;
import com.browxy.wrapper.message.Message;

public class CompileJavaMaven implements CompilerCode {
	private static final Logger logger = LoggerFactory.getLogger(CompileJavaMaven.class);

	@Override
	public CompilerResult compileUserCode(Message message) {
		try {
			String containerBasePath = System.getProperty("containerBasePath");
			String customRepoPath = System.getProperty("mavenRepoPath") != null ? System.getProperty("mavenRepoPath")
					: "/srv/maven";
			String customSettingsPath = System.getProperty("mavenSettingsPath") != null
					? System.getProperty("mavenSettingsPath")
					: "/srv/maven/settings.xml";
		   
			ProcessBuilder builder = new ProcessBuilder("mvn", "-Dmaven.repo.local=" + customRepoPath, "-s",
					customSettingsPath, "dependency:build-classpath", "clean", "compile");
			builder.directory(new File(containerBasePath));
			builder.redirectErrorStream(true);
			Process process = builder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
//			while ((line = reader.readLine()) != null) {
//				logger.info(line);
//			}
			
			 List<String> dependencyJars = new ArrayList<>();

			 while ((line = reader.readLine()) != null) {
				    logger.info(line);
				    if (line.startsWith("[INFO] Dependencies classpath:")) {
				        line = reader.readLine(); 
				        if (line != null) {
				            String[] jars = line.split(":");
				            for (String jar : jars) {
				                dependencyJars.add(jar.trim());
				            }
				        }
				    }
				}
		   
			int exitCode = process.waitFor();
			return new CompilerResultJava(exitCode == 0, dependencyJars);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error maven compile", e);
		}
		return new CompilerResultJava(false, Collections.emptyList());
	}

	
}
