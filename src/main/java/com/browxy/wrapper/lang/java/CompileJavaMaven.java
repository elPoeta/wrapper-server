package com.browxy.wrapper.lang.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.lang.CompilerCode;
import com.browxy.wrapper.lang.CompilerResult;
import com.browxy.wrapper.lang.CustomClassLoader;
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
					customSettingsPath, "clean", "compile", "dependency:build-classpath",
					"-Dmdep.outputFile=." + File.separator + "target/classpath.txt");
			builder.directory(new File(containerBasePath));
			builder.redirectErrorStream(true);
			Process process = builder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				logger.info(line);
			}

			int exitCode = process.waitFor();
			List<String> dependencyJars = exitCode == 0 ? Files
					.readAllLines(
							Paths.get(containerBasePath + File.separator + "target" + File.separator + "classpath.txt"))
					.stream().flatMap(lineFile -> Arrays.stream(lineFile.split(":"))).collect(Collectors.toList())
					: Collections.emptyList();

			dependencyJars.addAll(
					CustomClassLoader.getClasspathFromLibDirectory(containerBasePath + File.separator + "libraries"));

			return new CompilerResultJava(exitCode == 0, dependencyJars);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error maven compile", e);
		}
		return new CompilerResultJava(false, Collections.emptyList());
	}

}
