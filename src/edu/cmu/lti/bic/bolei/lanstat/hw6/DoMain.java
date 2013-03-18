package edu.cmu.lti.bic.bolei.lanstat.hw6;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntryIterator;
import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DecisionTreeNode;
import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DtUtil;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.NounQuestion;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.Question;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.VerbQuestion;

public class DoMain {
	public static void main(String[] args) throws IOException {
		int level = 3;

		// DecisionTreeNode root = DecisionTreeNode.initializeTree();
		DecisionTreeNode root = DecisionTreeNode.growDT(
				new LinkedList<Question>(Arrays.asList(new Question[] {
						new VerbQuestion(), new NounQuestion() })), level);

		// System.out.println(root.toString());
		Properties prop = DtUtil.getConfiguration();
		String filePath = prop.getProperty("corpusFilePath");
		int historySize = Integer.parseInt(DtUtil.getConfiguration()
				.getProperty("historySize"));
		DataSetEntryIterator it = new DataSetEntryIterator(filePath,
				historySize);
		double aveLogLik = root.calcAverageLogLikelihood(it);
		double perplexity = root.calcPerplexity(aveLogLik);
		System.out.println("on training data");
		System.out.println("level to grow: " + level);
		System.out.println("Average log likelihood: " + aveLogLik);
		System.out.println("Perplexity: " + perplexity);

	}
}
