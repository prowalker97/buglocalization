package jbnu.ssel.buglocater.preprocess.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import jbnu.ssel.buglocater.collect_data.dao.ClassCode;
import jbnu.ssel.buglocater.collect_data.dao.MethodCode;

public class ClassVisitor extends ASTVisitor {

	private List<ClassCode> classCodes;

	public ClassVisitor(List<ClassCode> prevClassCodes) {
		this.classCodes = prevClassCodes;
	}

	@Override
	public boolean visit(TypeDeclaration node) {

		SimpleName className = node.getName();
		String classNameAsString = className.toString();
		MethodDeclaration[] methods = node.getMethods();
		List<MethodCode> MethodCodes = new ArrayList<MethodCode>();
		for (MethodDeclaration methodDeclaration : methods) {
			MethodVisitor methodParser = new MethodVisitor(methodDeclaration, MethodCodes);
		}
		ClassCode classCode = new ClassCode(classNameAsString, MethodCodes);
		classCodes.add(classCode);
		return true;
	}
}
