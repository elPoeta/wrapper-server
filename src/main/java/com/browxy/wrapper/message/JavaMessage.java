package com.browxy.wrapper.message;

import com.browxy.wrapper.lang.java.CompileType;

public class JavaMessage extends BaseMessage implements Message {
	private CompileType compileType;
	private String classToLoad;
	
	public JavaMessage() {
		super();
	}

	public CompileType getCompileType() {
		return compileType;
	}

	public void setCompileType(CompileType compileType) {
		this.compileType = compileType;
	}

	public String getClassToLoad() {
		return classToLoad;
	}

	public void setClassToLoad(String classToLoad) {
		this.classToLoad = classToLoad;
	}
	
	
}
