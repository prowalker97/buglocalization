package jbnu.ssel.buglocater.collect_data.git;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

import jbnu.ssel.buglocater.collect_data.BuggyCommitCollector;
import jbnu.ssel.buglocater.collect_data.dao.BuggyCommit;
import jbnu.ssel.buglocater.collect_data.dao.ClassCode;
import jbnu.ssel.buglocater.dao.FixedCode;

public class BuggyCommitCollecterTest {
	
	public static void main(String[] args) throws NoHeadException, GitAPIException, IOException {
		BuggyCommitCollector bcc = new BuggyCommitCollector();
		List<BuggyCommit> buggyCommits = bcc.collectBuggyCommits("https://github.com/apache/camel-quarkus.git", "D:\\test\\camel-quarkus");
		for (BuggyCommit buggyCommit : buggyCommits) {
			List<ClassCode> curClassCodes = buggyCommit.getCurClassCodes();
			for (ClassCode curClassCode : curClassCodes) {
				System.out.println(curClassCode.getMethods().size());
			}
			List<ClassCode> prevClassCodes = buggyCommit.getPrevClassCodes();
			for (ClassCode prevClassCode : prevClassCodes) {
				System.out.println(prevClassCode.getMethods().size());
			}
			List<FixedCode> fixedCodes = buggyCommit.getFixedCodes();
			for (FixedCode fixedCode : fixedCodes) {
				System.out.println(fixedCode.getFixedMethods().size());
			}
			
		}
	}
}
