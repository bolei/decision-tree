package edu.cmu.lti.bic.bolei.lanstat.hw6;

import java.io.IOException;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DecisionTreeNode;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.VerbQuestion;

public class DoMain {
	public static void main(String[] args) throws IOException {
		DecisionTreeNode root = DecisionTreeNode.initializeTree();
		root.growDT(new VerbQuestion());
		System.out.println(root);
		if (root.isInMemory()) {
			root.dumpDataSet();
			root.getYesChild().dumpDataSet();
			root.getNoChild().dumpDataSet();
		}
	}

}
