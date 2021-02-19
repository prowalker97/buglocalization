package jbnu.ssel.buglocater.preprocess.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import jbnu.ssel.buglocater.collect_data.dao.MethodCode;

public class MethodVisitor {

	private MethodDeclaration methodDeclaration;

	public MethodVisitor(MethodDeclaration node, List<MethodCode> methodCodes) {
		methodDeclaration = node;
		Type returnType = node.getReturnType2();
		String returnTypeAsString = null;
		if(returnType!=null)
			returnTypeAsString = returnType.toString();
		SimpleName methodName = node.getName();
		String methodNameAsString = methodName.toString();
		List parameters = node.parameters();
		List<String> parameterNames = new ArrayList<String>();
		if (parameters.size() != 0) {
			for (Object parameter : parameters) {
				SingleVariableDeclaration singleVariableDecl = (SingleVariableDeclaration) parameter;
				String parameterName = singleVariableDecl.getName().toString();
				parameterNames.add(parameterName);
			}
		}
		List<String> comments = new ArrayList<String>();
		List<String> variableNames = new ArrayList<String>();
		parseMethodBody(comments, variableNames);
		MethodCode methodCode = new MethodCode(returnTypeAsString, methodNameAsString, parameterNames, comments,
				variableNames);
		methodCode.setMethodCode(node.toString());
		methodCodes.add(methodCode);

	}

	private void parseMethodBody(List<String> comments, List<String> variableNames) {
		if (methodDeclaration.getBody() == null)
			return;
		String methodBodyAsString = methodDeclaration.toString();

		String methodBodyWithTemplate = "public class AA{ "
				+ methodBodyAsString.substring(0, methodBodyAsString.length() - 1) + "}";

		final ASTSupportSingleton astSupport = ASTSupportSingleton.getInstance();
		astSupport.parse(methodBodyWithTemplate, new ASTVisitor() {

			public boolean  visit(VariableDeclarationFragment node) {
				String variableName = node.getName().toString();
				variableNames.add(variableName);
				return true;
			}
			
			public boolean visit(Comment node) {
				String comment = node.toString();
				comments.add(comment);
				return true;
			}

		});
	}

	public String getMethodBody() {
		return methodDeclaration.toString();
	}

}
