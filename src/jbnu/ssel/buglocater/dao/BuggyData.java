package jbnu.ssel.buglocater.dao;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

import jbnu.ssel.buglocater.collect_data.dao.BugReport;

public class BuggyData {
	
	private String bugID;
	private BugReport bugReport;
	private List<FixedCode> fixedCodes;
	private String commitLog;
	
	public String getBugID() {
		return bugID;
	}
	public void setBugID(String bugID) {
		this.bugID = bugID;
	}
	public BugReport getBugReport() {
		return bugReport;
	}
	public void setBugReport(BugReport bugReport) {
		this.bugReport = bugReport;
	}
	public List<FixedCode> getFixedFiles() {
		return fixedCodes;
	}
	public void setFixedFiles(List<FixedCode> fixedFiles) {
		this.fixedCodes = fixedFiles;
	}
	public String getCommitLog() {
		return commitLog;
	}
	public void setCommitLog(String commitLog) {
		this.commitLog = commitLog;
	}
	
}
