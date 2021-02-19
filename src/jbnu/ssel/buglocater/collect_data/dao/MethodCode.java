package jbnu.ssel.buglocater.collect_data.dao;

import java.util.List;

public class MethodCode {

	private String returnType;
	private String methodName;
	private List<String> parameterNames;
	private List<String> comments;
	private List<String> variableNames;
	private String methodCode;
	
	public MethodCode(String returnType, String methodName, List<String> parameterNames, List<String> comments, List<String> variableNames) {
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
	public String getMethodCode() {
		return methodCode;
	}

	public void setMethodCode(String methodCode) {
		this.methodCode = methodCode;
	}

	public String getMethodIdentifier() {
		StringBuilder parametersString = new StringBuilder("");
		for (int i = 0; i < parameterNames.size(); i++) {
			String parameter = parameterNames.get(i);
			if (i != parameterNames.size() - 1)
				parametersString.append(parameter + ", ");
			else
				parametersString.append(parameter);
		}
		if(returnType != null)
			return returnType +" "+ methodName +"("+ parametersString.toString()+")";
		else
			return methodName +"("+ parametersString.toString()+")";
	}
}
