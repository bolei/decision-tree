package edu.cmu.lti.bic.bolei.lanstat.hw6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntryIterator;
import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DecisionTree;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.Question;

public class Problem5 {
	public static void main(String[] args) throws IOException {
		System.out.println("==============problem 5 ================");
		// generating questions
		LinkedList<Question> questions = new LinkedList<Question>();
		questions.addAll(DtUtil.generateNGramQuestions(1));
		questions.addAll(DtUtil.generateNGramQuestions(2));
		questions.addAll(DtUtil.generateContainsNWordsQuestions(1));
		questions.addAll(DtUtil.generateContainsNWordsQuestions(2));

		// generate tree
		DecisionTree tree = new DecisionTree(questions);
		tree.growDT(3); // grow to 3 level tree

		Properties prop = DtUtil.getConfiguration();

		// prepare training data
		String corpusFilePath = prop.getProperty("corpusFilePath");
		int historySize = Integer.parseInt(DtUtil.getConfiguration()
				.getProperty("historySize"));
		ArrayList<String> corpusData = DtUtil.getCorpusData(corpusFilePath);
		DataSetEntryIterator it = new DataSetEntryIterator(corpusData,
				historySize);
		double aveLogLik = tree.calcAverageLogLikelihood(it);
		double perplexity = tree.calcPerplexity(aveLogLik);
		System.out.println("on training data");
		System.out.println("Average log likelihood: " + aveLogLik);
		System.out.println("Perplexity: " + perplexity);

		// prepare test data
		System.out.println("==========");
		corpusFilePath = prop.getProperty("testFilePath");
		corpusData = DtUtil.getCorpusData(corpusFilePath);
		it = new DataSetEntryIterator(corpusData, historySize);
		aveLogLik = tree.calcAverageLogLikelihood(it);
		perplexity = tree.calcPerplexity(aveLogLik);
		System.out.println("on test data");
		System.out.println("Average log likelihood: " + aveLogLik);
		System.out.println("Perplexity: " + perplexity);

	}
}
