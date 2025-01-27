package com.browxy.wrapper.lang;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CustomClassLoader extends URLClassLoader {
	private File targetDir;

	public CustomClassLoader(File targetDir, ClassLoader parent) throws MalformedURLException {
		super(new URL[] { targetDir.toURI().toURL() }, parent);
		this.targetDir = targetDir;
	}

	public CustomClassLoader(File targetDir, URL[] urls, ClassLoader parent) {
		super(urls, parent);
		this.targetDir = targetDir;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println("Loading class: " + name);
		System.out.println("ClassLoader hierarchy:");
		ClassLoader classLoader = this;
		while (classLoader != null) {
			System.out.println(classLoader);
			classLoader = classLoader.getParent();
		}
		try {
			String className = name.replace('.', '/') + ".class";
			File classFile = new File(targetDir, className);
			if (classFile.exists()) {
				byte[] classBytes = Files.readAllBytes(classFile.toPath());
				return defineClass(name, classBytes, 0, classBytes.length);
			}
		} catch (IOException e) {
			throw new ClassNotFoundException(name, e);
		}
		return super.findClass(name);
	}

	public static CustomClassLoader createClassLoader(File targetDir, ClassLoader parent, List<String> dependencyJars)
			throws MalformedURLException {
		CustomClassLoader classLoader = new CustomClassLoader(targetDir, new URL[] { targetDir.toURI().toURL() },
				parent);

		for (String dependencyJar : dependencyJars) {
			classLoader.addURL(new File(dependencyJar).toURI().toURL());
		}

		return classLoader;
	}

	public static List<String> getClasspathFromLibDirectory(String libDirectory) {
		List<String> jarFiles = new ArrayList<>();
		File libDir = new File(libDirectory);
		if (libDir.exists() && libDir.isDirectory()) {
			File[] files = libDir.listFiles((dir, name) -> name.endsWith(".jar"));

			if (files != null) {
				for (File file : files) {
					jarFiles.add(file.getAbsolutePath());
				}
			}
		}

		return jarFiles;
	}
}
