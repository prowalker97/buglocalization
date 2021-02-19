package jbnu.ssel.buglocater.dao;

import java.util.List;

public class FixedMethodCode {
	private String returnType;
	private String methodName;
	private List<String> parameterNames;
	private List<String> comments;
	private List<String> variableNames;
	
	public FixedMethodCode(String returnType, String methodName, List<String> parameterNames, List<String> comments, List<String> variableNames) {
		this.returnType = returnType;
		this.methodName = methodName;
		this.parameterNames = parameterNames;
		this.comments = comments;
		this.variableNames = variableNames;
	}

	public String getReturnType() {
		return returnType;
	}

	public String getMethodName() {
		return methodName;
	}

	public List<String> getParameterNames() {
		return parameterNames;
	}

	public List<String> getComments() {
		return comments;
	}

	public List<String> getVariableNames() {
		return variableNames;
	}
	
}
