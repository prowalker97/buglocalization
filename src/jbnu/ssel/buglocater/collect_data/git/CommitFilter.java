package jbnu.ssel.buglocater.collect_data.git;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import jbnu.ssel.buglocater.collect_data.dao.BuggyCommit;
import jbnu.ssel.buglocater.util.Constants;

public class CommitFilter {

	private Repository repo;
	private Git git;
	private RevWalk walk;
	private CommitDiffGenerator commitDiffGenerator;

	public CommitFilter(GitRepositoryGenerator gitRepositoryGenerator) {
		git = gitRepositoryGenerator.getGit();
		repo = gitRepositoryGenerator.getRepo();
		walk = gitRepositoryGenerator.getWalk();
		commitDiffGenerator = new CommitDiffGenerator(gitRepositoryGenerator);
	}

	public List<BuggyCommit> filterNonBuggyCommits(Iterable<RevCommit> commits)
			throws IOException, GitAPIException {
		List<BuggyCommit> BuggyCommitList = new ArrayList<BuggyCommit>();
		RevCommit targetCommit = null;
		for (RevCommit commit : commits) {
			if (targetCommit != null) {
				String commitMsg = targetCommit.getFullMessage();
				AbstractTreeIterator newTreeIterator = getCanonicalTreeParser(targetCommit);
				AbstractTreeIterator oldTreeIterator = getCanonicalTreeParser(commit);
				List<DiffEntry> diffs = git.diff().setNewTree(newTreeIterator).setOldTree(oldTreeIterator).call();
				String[] buggyWords = Constants.BUGGY_WORDS;
				for (String buggyWord : buggyWords) {
					if (commitMsg.contains(buggyWord)) {
//						System.out.println("Commit MSG:" + commitMsg);
						HashMap<DiffEntry, String> diffContents = commitDiffGenerator.generateDiffContents(diffs);
						BuggyCommitList.add(new BuggyCommit(commit, targetCommit, diffContents));
					}
				}
			}
			targetCommit = commit;
		}
		return BuggyCommitList;
	}

	private AbstractTreeIterator getCanonicalTreeParser(ObjectId commitId) throws IOException {
		RevCommit commit = walk.parseCommit(commitId);
		ObjectId treeId = commit.getTree().getId();
		ObjectReader reader = repo.newObjectReader();
		return new CanonicalTreeParser(null, reader, treeId);
	}
}
