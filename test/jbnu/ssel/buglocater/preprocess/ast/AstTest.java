package jbnu.ssel.buglocater.preprocess.ast;

import java.util.ArrayList;
import java.util.List;

import jbnu.ssel.buglocater.collect_data.dao.ClassCode;
import jbnu.ssel.buglocater.collect_data.dao.MethodCode;

public class AstTest {

	static String source = "public abstract class MisraTableViewColumn extends ColumnLabelProvider {\r\n"
			+ "  public abstract String getText(  Object element);\r\n" + "  public abstract String getTitle();\r\n"
			+ "  public abstract int getWidth();\r\n"
			+ "  public TableViewerColumn addColumnTo(  TableViewer tableViewer){\r\n"
			+ "    TableViewerColumn tableViewerColumn=new TableViewerColumn(tableViewer,SWT.NONE);\r\n"
			+ "    TableColumn column = tableViewerColumn.getColumn();\r\n" + "    column.setMoveable(false);\r\n"
			+ "    column.setResizable(false);\r\n" + "    column.setText(getTitle());\r\n"
			+ "    column.setWidth(getWidth());\r\n" + "    tableViewerColumn.setLabelProvider(this);\r\n"
			+ "    return tableViewerColumn;\r\n" + "  }\r\n" + "}";

	public static void main(String[] args) {
		ASTSupportSingleton astSupport = ASTSupportSingleton.getInstance();
		List<ClassCode> classCodes = new ArrayList<ClassCode>();
		astSupport.parse(source, new ClassVisitor(classCodes));
		for (ClassCode classCode : classCodes) {
			System.out.println("Fixed Code Class Name:  " + classCode.getClassName());
			List<MethodCode> methods = classCode.getMethods();
			for (MethodCode method : methods) {
				List<String> parameters = method.getParameterNames();
				StringBuilder parametersString = new StringBuilder();
				for (int i = 0; i < parameters.size(); i++) {
					String parameter = parameters.get(i);
					if (i != parameters.size() - 1)
						parametersString.append(parameter + ",");
					else
						parametersString.append(parameter);
				}

				System.out.println("Fixed Method ReturnType: " + method.getReturnType() + "  Method Name:"
						+ method.getMethodName() + "  Parameters:  " + parametersString.toString() + "  # of Variable: "
						+ method.getVariableNames().size() + "  # of Comments: " + method.getComments().size());
			}
		}
	}
}
