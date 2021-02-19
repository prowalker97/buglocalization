package jbnu.ssel.buglocater.collect_data.git;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.reftree.Command;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

public class CommitCollector {

	private Repository repo;
	private Git git;
	private Ref masterBranch;

	public CommitCollector(GitRepositoryGenerator gitRepositoryGenerator) {
		git = gitRepositoryGenerator.getGit();
		repo = gitRepositoryGenerator.getRepo();
	}

	// This method extract commits for master branch
	public Iterable<RevCommit> collectCommits() throws NoHeadException, GitAPIException, IOException {
		Iterable<RevCommit> commits = null;
		masterBranch = getMasterBranch(git);
		if (masterBranch != null) {
			String masterBranchName = masterBranch.getName();
			System.out.println("Commits of branch: " + masterBranchName);
			System.out.println("-------------------------------------");

			commits = git.log().add(repo.resolve(masterBranchName)).call();
		}
		return commits;
	}

	public RevCommit getCommit(String commitID) {
		RevCommit commit = null;
		try {
			RevWalk walk = new RevWalk(repo);
			ObjectId id = null;
			id = repo.resolve(commitID);
			commit = walk.parseCommit(id);
			System.out.println("Found Commit again: " + commit);
			walk.dispose();
		} catch (RevisionSyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commit;
	}

	private Ref getMasterBranch(Git git) {
		List<Ref> branches;
		Ref masterBrunch = null;
		try {
			branches = git.branchList().call();
			for (Ref branch : branches) {
				String branchName = branch.getName();
				if (branchName.contains(Constants.MASTER)) {
					masterBrunch = branch;
					break;
				}
				if (branches.size() == 1) {
					masterBrunch = branch;
					break;
				}
			}
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return masterBrunch;
	}

}
