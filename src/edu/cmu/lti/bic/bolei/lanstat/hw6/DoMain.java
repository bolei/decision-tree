package edu.cmu.lti.bic.bolei.lanstat.hw6;

import java.io.IOException;
import java.util.Properties;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntryIterator;
import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DecisionTreeNode;
import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DtUtil;

public class DoMain {
	public static void main(String[] args) throws IOException {
		DecisionTreeNode root = DecisionTreeNode.initializeTree();
		Properties prop = DtUtil.getConfiguration();
		String corpusFilePath = prop.getProperty("corpusFilePath");
		int historySize = Integer.parseInt(DtUtil.getConfiguration()
				.getProperty("historySize"));
		DataSetEntryIterator it = new DataSetEntryIterator(corpusFilePath,
				historySize);
		double aveLogLik = root.calcAverageLogLikelihood(it);
		double perplexity = root.calcPerplexity(aveLogLik);
		System.out.println("on training data");
		System.out.println("Average log likelihood: " + aveLogLik);
		System.out.println("Perplexity: " + perplexity);
		// double mutualInfo = root.tryGrowDT(new VerbQuestion());
		// System.out.println("root mutual info: " + mutualInfo);
	}

}
