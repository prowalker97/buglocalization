package jbnu.ssel.buglocater.preprocess.bugzilla;

import java.util.Iterator;
import java.util.List;


import b4j.core.Issue;
import b4j.core.LongDescription;

public class BugZillaSessionTest {
	public static void main(String[] args) {
		BugZillaSession bzs = new BugZillaSession();
		bzs.newInitSession();
		Iterator<Issue> issues = bzs.newSearchBugs("RHQ Project");
		while (issues.hasNext()) {
			Issue issue = (Issue) issues.next();
			Iterator<LongDescription> longDescriptions = issue.getLongDescriptionIterator();
			while (longDescriptions.hasNext()) {
				LongDescription longDescription = (LongDescription) longDescriptions.next();
				System.out.println("----------------------------------");
				System.out.println(longDescription.getTheText());
			}
		}
		bzs.closeSession();
	}
}
