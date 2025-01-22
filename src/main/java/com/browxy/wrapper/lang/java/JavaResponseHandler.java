package com.browxy.wrapper.lang.java;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.lang.CompilerCode;
import com.browxy.wrapper.message.JavaMessage;
import com.browxy.wrapper.message.Message;
import com.browxy.wrapper.response.ResponseMessage;
import com.browxy.wrapper.response.ResponseMessageUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

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
			return ResponseMessageUtil.getStatusMessage(
					"Failed to load user code. File: " + userCodeFile.getAbsolutePath());
		}

		try {
			String result = callMethod(userClass, message.getMethod(), message.getArguments());
			return result;

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

			boolean compiled = this.compilerCode.compileUserCode(this.message);

			if (!compiled) {
				logger.error("Error compiling user code.");
				return null;
			}

			URLClassLoader classLoader = null;
			try {
				File targetDir = new File(containerBasePath + "/target/classes");

				classLoader = new URLClassLoader(new URL[] { targetDir.toURI().toURL() },
						this.getClass().getClassLoader());

				cachedUserClass = classLoader.loadClass(message.getClassToLoad());
			} catch (ClassNotFoundException | MalformedURLException e) {
				e.printStackTrace();
				logger.error("Error loading user class", e);
				return null;
			} finally {
				if (classLoader != null) {
					try {
						classLoader.close();
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("Error close classLoader", e);
					}
				}
			}
		}

		return cachedUserClass;
	}

	public String callMethod(Class<?> clazz, String methodName, String argumentsJson) throws Exception {
		JsonArray jsonArray = validateAndParseJsonArray(argumentsJson);
		List<Object> arguments = new ArrayList<>();
		List<Class<?>> argumentTypes = new ArrayList<>();

		for (JsonElement element : jsonArray) {
			if (element.isJsonPrimitive()) {
				JsonPrimitive primitive = element.getAsJsonPrimitive();
				if (primitive.isNumber()) {
					// arguments.add(primitive.getAsNumber());
					// argumentTypes.add(Number.class);
					Number number;
					Class<?> numberType;
					try {
						if (primitive.getAsString().contains(".")) {
							double doubleValue = primitive.getAsDouble();
							if (doubleValue == (float) doubleValue) {
								number = (float) doubleValue;
								numberType = float.class;
							} else {
								number = doubleValue;
								numberType = double.class;
							}
						} else {
							long longValue = primitive.getAsLong();
							if (longValue == (int) longValue) {
								number = (int) longValue;
								numberType = int.class;
							} else {
								number = longValue;
								numberType = long.class;
							}
						}
					} catch (NumberFormatException e) {
						number = primitive.getAsNumber();
						numberType = Number.class;
					}

					arguments.add(number);
					argumentTypes.add(numberType);
				} else if (primitive.isString()) {
					arguments.add(primitive.getAsString());
					argumentTypes.add(String.class);
				} else if (primitive.isBoolean()) {
					arguments.add(primitive.getAsBoolean());
					argumentTypes.add(Boolean.class);
				}
			} else if (element.isJsonObject()) {
				arguments.add(new Gson().fromJson(element, Object.class));
				argumentTypes.add(Object.class);
			} else if (element.isJsonArray()) {
				JsonArray jsonArrayNested = element.getAsJsonArray();
				List<Object> list = new ArrayList<>();
				for (JsonElement arrayElement : jsonArrayNested) {
					list.add(parseJsonElement(arrayElement));
				}
				arguments.add(list);
				argumentTypes.add(List.class);
			} else if (element.isJsonNull()) {
				arguments.add(null);
				argumentTypes.add(Object.class);
			}
		}

		Class<?>[] parameterTypes = argumentTypes.toArray(new Class[0]);

		Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
		method.setAccessible(true);

		Object instance = null;
		if (!Modifier.isStatic(method.getModifiers())) {
			instance = clazz.getDeclaredConstructor().newInstance();
		}

		Object result = method.invoke(instance, arguments.toArray());

		return result != null ? result.toString() : "null";
	}

	private JsonArray validateAndParseJsonArray(String argumentsJson) {
		if (argumentsJson == null || argumentsJson.trim().isEmpty()) {
			return new JsonArray();
		}
		try {
			JsonElement jsonElement = JsonParser.parseString(argumentsJson);
			if (jsonElement.isJsonArray()) {
				return jsonElement.getAsJsonArray();
			}

		} catch (Exception e) {
			logger.error("The json array parser throw an error ", e);
		}
		return new JsonArray();
	}

	private Object parseJsonElement(JsonElement element) {
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isNumber()) {
				return primitive.getAsNumber();
			} else if (primitive.isString()) {
				return primitive.getAsString();
			} else if (primitive.isBoolean()) {
				return primitive.getAsBoolean();
			}
		} else if (element.isJsonObject()) {
			return new Gson().fromJson(element, Object.class);
		} else if (element.isJsonArray()) {
			JsonArray array = element.getAsJsonArray();
			List<Object> list = new ArrayList<>();
			for (JsonElement arrayElement : array) {
				list.add(parseJsonElement(arrayElement));
			}
			return list;
		}
		return null;
	}

}
