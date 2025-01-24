package com.browxy.wrapper.lang.java;

import java.util.List;

import com.browxy.wrapper.lang.CompilerResult;

public class CompilerResultJava implements CompilerResult {
	private boolean success;
	private List<String> dependencyJars;

	public CompilerResultJava() {

	}

	public CompilerResultJava(boolean success, List<String> dependencyJars) {
		this.success = success;
		this.dependencyJars = dependencyJars;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<String> getDependencyJars() {
		return dependencyJars;
	}

	public void setDependencyJars(List<String> dependencyJars) {
		this.dependencyJars = dependencyJars;
	}

}
