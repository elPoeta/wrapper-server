package com.browxy.wrapper.lang.java;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.lang.CompilerCode;
import com.browxy.wrapper.lang.CustomClassLoader;
import com.browxy.wrapper.lang.InvokeResult;
import com.browxy.wrapper.message.JavaMessage;
import com.browxy.wrapper.message.Message;
import com.browxy.wrapper.response.ResponseMessage;
import com.browxy.wrapper.response.ResponseMessageUtil;

public class JavaResponseHandler implements ResponseMessage {
	private static final Logger logger = LoggerFactory.getLogger(JavaResponseHandler.class);

	private JavaMessage message;
	private CompilerCode compilerCode;
	private static long lastModified = 0;
	private static Class<?> cachedUserClass = null;

	public JavaResponseHandler(Message message) {
		this.message = (JavaMessage) message;
		this.compilerCode = this.message.getCompileType().equals(CompileType.Pom) ? new CompileJavaMaven()
				: new CompileJavaStandard();
	}

	@Override
	public String handleClientRequest() {
		Class<?> userClass = getCachedOrCompileUserClass();
		if (userClass == null) {
			String containerBasePath = System.getProperty("containerBasePath");
			File userCodeFile = new File(containerBasePath + message.getUserCodePath());
			return ResponseMessageUtil
					.getStatusMessage("Failed to load user code. File: " + userCodeFile.getAbsolutePath());
		}

		try {
			InvokeResult invokeResult = callMethod(userClass, message.getMethod(), message.getArguments());
			if(invokeResult.hasError()) {
				return ResponseMessageUtil.getStatusMessage(invokeResult.getMessage());
			}
			return invokeResult.getResult();

		} catch (Exception e) {
			logger.error("Error executing user code:", e);
			return ResponseMessageUtil.getStatusMessage("Error executing user code: " + e.getMessage());
		}
	}

	private Class<?> getCachedOrCompileUserClass() {
		String containerBasePath = System.getProperty("containerBasePath");
		File userCodeFile = new File(containerBasePath + message.getUserCodePath());

		if (cachedUserClass == null || userCodeFile.lastModified() > lastModified) {
			logger.info("User code change detected. Recompiling...");

			lastModified = userCodeFile.lastModified();

			CompilerResultJava compilerResult = (CompilerResultJava) this.compilerCode.compileUserCode(this.message);

			if (!compilerResult.isSuccess()) {
				logger.error("Error compiling user code.");
				return null;
			}

			try {
				File targetDir = new File(containerBasePath + "/target/classes");

				CustomClassLoader classLoader = CustomClassLoader.createClassLoader(targetDir,
						this.getClass().getClassLoader(), compilerResult.getDependencyJars());
				cachedUserClass = classLoader.loadClass(message.getClassToLoad());
			} catch (ClassNotFoundException | MalformedURLException e) {
				e.printStackTrace();
				logger.error("Error loading user class", e);
				return null;
			}
		}

		return cachedUserClass;
	}

	public InvokeResult callMethod(Class<?> clazz, String methodName, String argumentsJson) throws Exception {
		String message = "";
		try {
			ParseStringJavaArgs parseStringJavaArgs = new ParseStringJavaArgs(argumentsJson);
			parseStringJavaArgs.parseArgs();
			Class<?>[] parameterTypes = parseStringJavaArgs.getParameterTypes();

			Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);

			Object instance = null;
			if (!Modifier.isStatic(method.getModifiers())) {
				instance = clazz.getDeclaredConstructor().newInstance();
			}

			Object result = method.invoke(instance, parseStringJavaArgs.getArguments().toArray());

			String res = result != null ? result.toString() : null;
			return new InvokeResult(res, 200, message);
		} catch (InvocationTargetException e) {
			logger.error("error target invocation", e);
			message = "error target invocation " + e.getMessage();
		} catch (Exception e) {
			logger.error("error call method", e);
			message = "error call method " + e.getMessage();
		}
		return new InvokeResult(null, 400, message);
	}

}
