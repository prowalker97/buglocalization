package jbnu.ssel.buglocater.collect_data.git;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import jbnu.ssel.buglocater.collect_data.dao.BuggyCommit;
import jbnu.ssel.buglocater.collect_data.dao.ClassCode;
import jbnu.ssel.buglocater.dao.FixedMethodCode;
import jbnu.ssel.buglocater.preprocess.ast.ASTSupportSingleton;
import jbnu.ssel.buglocater.preprocess.ast.ClassVisitor;

public class ChangedCodeExtractor {

	private Repository repo;

	public ChangedCodeExtractor(GitRepositoryGenerator gitRepositoryGenerator) {
		repo = gitRepositoryGenerator.getRepo();
	}

	public void collectSourceCodes(BuggyCommit buggyCommit, ASTSupportSingleton astSupport)
			throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		RevCommit curCommit = buggyCommit.getCurCommit();
		RevCommit prevCommit = buggyCommit.getPrevCommit();
		HashMap<DiffEntry, String> diffContents = buggyCommit.getDiffContents();
		Set<Entry<DiffEntry, String>> diffEntryContents = diffContents.entrySet();
		List<ClassCode> prevClassCodes = new ArrayList<ClassCode>();
		List<ClassCode> curClassCodes = new ArrayList<ClassCode>();
		
		for (Entry<DiffEntry, String> diffEntryContent : diffEntryContents) {
			DiffEntry diff = diffEntryContent.getKey();
			// This is new file
			String prevChangedFilePath = diff.getNewPath();
			String prevVersionSourceCode = null;
			if (!prevChangedFilePath.equals(diff.DEV_NULL))
				prevVersionSourceCode = extractChangedFileSourceCode(prevCommit, prevChangedFilePath);
			// This is old file			
			String curChangedFilePath = diff.getOldPath();
			String curVersionSourceCode = null;
			if (!curChangedFilePath.equals(diff.DEV_NULL))
				curVersionSourceCode = extractChangedFileSourceCode(curCommit, curChangedFilePath);

			if (prevVersionSourceCode != null) {
				astSupport.parse(prevVersionSourceCode, new ClassVisitor(prevClassCodes));
			}
			if (curVersionSourceCode != null) {
				astSupport.parse(curVersionSourceCode, new ClassVisitor(curClassCodes));
			}
		}
		buggyCommit.setPrevClassCodes(prevClassCodes);
		buggyCommit.setCurClassCodes(curClassCodes);
	}
	
//	This method retrieve the certain file at the commit and generate file to String type for AST Parser.
	private String extractChangedFileSourceCode(RevCommit commit, String filePath)
			throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		// and using commit's tree find the path
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(repo);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.create(filePath));
		if (!treeWalk.next()) {
			return null;
		}
		ObjectId objectId = treeWalk.getObjectId(0);
		ObjectLoader loader = repo.open(objectId);
		String sourceCode = readStream(loader.openStream());

		return sourceCode;
	}

	private String readStream(InputStream iStream) throws IOException {
		// build a Stream Reader, it can read char by char
		InputStreamReader iStreamReader = new InputStreamReader(iStream);
		// build a buffered Reader, so that i can read whole line at once
		BufferedReader bReader = new BufferedReader(iStreamReader);
		String line = null;
		StringBuilder builder = new StringBuilder();
		while ((line = bReader.readLine()) != null) { // Read till end
			builder.append(line + '\n');
		}
		bReader.close(); // close all opened stuff
		iStreamReader.close();
		iStream.close();
		return builder.toString();
	}

}
