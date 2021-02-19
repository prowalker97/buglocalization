package jbnu.ssel.buglocater.dao;

import java.util.List;

public class FixedCode {
	private String className;
	private List<FixedMethodCode> fixedMethods;
	
	public FixedCode(String className, List<FixedMethodCode> fixedMethods) {
		super();
		this.className = className;
		this.fixedMethods = fixedMethods;
	}

	public String getClassName() {
		return className;
	}

	public List<FixedMethodCode> getFixedMethods() {
		return fixedMethods;
	}
	
}
