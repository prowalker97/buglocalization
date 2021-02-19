package jbnu.ssel.buglocater.collect_data.dao;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

import jbnu.ssel.buglocater.dao.FixedCode;

public class BuggyCommit {

	private RevCommit targetCommit;
	private RevCommit prevCommmit;
	private HashMap<DiffEntry, String> diffContents;
	private List<ClassCode> PrevClassCodes;
	private List<ClassCode> curClassCodes;
	private List<FixedCode> FixedCodes;

	public BuggyCommit(RevCommit prevCommit, RevCommit targetCommit, HashMap<DiffEntry, String> diffContents) {
		// TODO Auto-generated constructor stub
		this.prevCommmit = prevCommit;
		this.targetCommit = targetCommit;
		this.diffContents = diffContents;
	}
	
	public RevCommit getCurCommit() {
		return this.targetCommit;
	}
	
	public RevCommit getPrevCommit() {
		return this.prevCommmit;
	}

	public HashMap<DiffEntry, String> getDiffContents() {
		return diffContents;
	}

	public List<ClassCode> getPrevClassCodes() {
		return PrevClassCodes;
	}

	public void setPrevClassCodes(List<ClassCode> prevClassCodes) {
		PrevClassCodes = prevClassCodes;
	}

	public List<ClassCode> getCurClassCodes() {
		return curClassCodes;
	}

	public void setCurClassCodes(List<ClassCode> curClassCodes) {
		this.curClassCodes = curClassCodes;
	}

	public void setFixedCodes(List<FixedCode> fixedCodes) {
		this.FixedCodes = fixedCodes;
	}

	public List<FixedCode> getFixedCodes() {
		return FixedCodes;
	}

}
