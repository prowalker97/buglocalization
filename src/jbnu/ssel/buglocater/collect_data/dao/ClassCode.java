package jbnu.ssel.buglocater.collect_data.dao;

import java.util.List;


public class ClassCode {
	private String className;
	private List<MethodCode> methods;
	
	public ClassCode(String className, List<MethodCode> methodCodes) {
		super();
		this.className = className;
		this.methods = methodCodes;
	}

	public String getClassName() {
		return className;
	}

	public List<MethodCode> getMethods() {
		return methods;
	}
	
}
