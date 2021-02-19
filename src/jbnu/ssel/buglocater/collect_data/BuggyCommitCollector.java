package jbnu.ssel.buglocater.collect_data;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import jbnu.ssel.buglocater.collect_data.dao.BuggyCommit;
import jbnu.ssel.buglocater.collect_data.dao.ClassCode;
import jbnu.ssel.buglocater.collect_data.git.ChangedCodeExtractor;
import jbnu.ssel.buglocater.collect_data.git.CommitCollector;
import jbnu.ssel.buglocater.collect_data.git.CommitFilter;
import jbnu.ssel.buglocater.collect_data.git.GitRepositoryGenerator;
import jbnu.ssel.buglocater.dao.FixedCode;
import jbnu.ssel.buglocater.dao.BuggyData;
import jbnu.ssel.buglocater.preprocess.ast.ASTSupportSingleton;
import jbnu.ssel.buglocater.preprocess.ast.FixedMethodsExtractor;

public class BuggyCommitCollector {

	public List<BuggyCommit> collectBuggyCommits(String URL, String localDir) {
		List<BuggyCommit> buggyCommits = null;
		try {
			GitRepositoryGenerator gitRepositoryGen = new GitRepositoryGenerator(URL, localDir);
			CommitCollector commitChangesExtractor = new CommitCollector(gitRepositoryGen);
			Iterable<RevCommit> commits = commitChangesExtractor.collectCommits();
			CommitFilter cf = new CommitFilter(gitRepositoryGen);
			// this method filter nonBuggy commits and collect buggyCommits			
			buggyCommits = cf.filterNonBuggyCommits(commits);
			ChangedCodeExtractor cce = new ChangedCodeExtractor(gitRepositoryGen);
			ASTSupportSingleton astSupport = ASTSupportSingleton.getInstance();
			for (BuggyCommit buggyCommit : buggyCommits) {
				cce.collectSourceCodes(buggyCommit, astSupport);
				List<ClassCode> curClassCodes = buggyCommit.getCurClassCodes();
				List<ClassCode> prevClassCodes = buggyCommit.getPrevClassCodes();
				FixedMethodsExtractor fme = new FixedMethodsExtractor();
				List<FixedCode> fixedCodes = fme.extractFiexMethods(curClassCodes, prevClassCodes);
				buggyCommit.setFixedCodes(fixedCodes);
			}
		} catch (GitAPIException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buggyCommits;
	}

}
