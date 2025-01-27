package com.browxy.wrapper.lang.java;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class ParseStringJavaArgs {
	private static final Logger logger = LoggerFactory.getLogger(ParseStringJavaArgs.class);

	private JsonArray jsonArray;
	private List<Object> arguments = new ArrayList<>();
	private List<Class<?>> argumentTypes = new ArrayList<>();

	public ParseStringJavaArgs(String argumentsJson) {
		this.jsonArray = this.validateAndParseJsonArray(argumentsJson);
	}

	public JsonArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JsonArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public List<Object> getArguments() {
		return arguments;
	}

	public void setArguments(List<Object> arguments) {
		this.arguments = arguments;
	}

	public List<Class<?>> getArgumentTypes() {
		return argumentTypes;
	}

	public void setArgumentTypes(List<Class<?>> argumentTypes) {
		this.argumentTypes = argumentTypes;
	}

	public Class<?>[] getParameterTypes() {
		return this.argumentTypes.toArray(new Class[0]);
	}

	public void parseArgs() {
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
