package jbnu.ssel.buglocater.preprocess.ast;

import java.util.ArrayList;
import java.util.List;

import jbnu.ssel.buglocater.collect_data.dao.ClassCode;
import jbnu.ssel.buglocater.collect_data.dao.MethodCode;
import jbnu.ssel.buglocater.dao.FixedCode;
import jbnu.ssel.buglocater.dao.FixedMethodCode;

public class FixedMethodsExtractor {

	public List<FixedCode> extractFiexMethods(List<ClassCode> curClassCodes, List<ClassCode> prevClassCodes) {
		List<FixedCode> buggyCodes = new ArrayList<FixedCode>();
		for (ClassCode curClassCode : curClassCodes) {
			List<FixedMethodCode> buggyMethods = new ArrayList<FixedMethodCode>();
			String curClassName = curClassCode.getClassName();
			for (ClassCode prevClassCode : prevClassCodes) {
				String prevClassName = prevClassCode.getClassName();
				if (curClassName.equals(prevClassName)) {
					List<MethodCode> curMethods = curClassCode.getMethods();
					List<MethodCode> prevMethods = prevClassCode.getMethods();
					for (MethodCode curMethod : curMethods) {
						String curMethodIden = curMethod.getMethodIdentifier();
						String curMethodCode = curMethod.getMethodCode();
						for (MethodCode prevMethod : prevMethods) {
							String prevMethodIden = prevMethod.getMethodIdentifier();
							String prevMethodCode = prevMethod.getMethodCode();
							if (curMethodIden.equals(prevMethodIden)) {
								if (!curMethodCode.equals(prevMethodCode)) {
									FixedMethodCode bmc = new FixedMethodCode(curMethod.getReturnType(),
											curMethod.getMethodName(), curMethod.getParameterNames(),
											curMethod.getComments(), curMethod.getVariableNames());
									buggyMethods.add(bmc);
								}
							}
						}
					}
				}
			}
			if(buggyMethods.size() != 0) {
				FixedCode bc = new FixedCode(curClassCode.getClassName(), buggyMethods);
				buggyCodes.add(bc);
			}
		}
		return buggyCodes;
	}
}
