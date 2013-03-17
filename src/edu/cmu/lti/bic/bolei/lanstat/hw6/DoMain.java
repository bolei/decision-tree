package edu.cmu.lti.bic.bolei.lanstat.hw6;

import java.io.IOException;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DecisionTreeNode;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.VerbQuestion;

public class DoMain {
	public static void main(String[] args) throws IOException {
		DecisionTreeNode root = DecisionTreeNode.initializeTree();
		double mutualInfo = root.tryGrowDT(new VerbQuestion());
		System.out.println("root mutual info: " + mutualInfo);
	}

}
