package edu.cmu.lti.bic.bolei.lanstat.hw6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntryIterator;
import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DecisionTree;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.Question;

public class DoMain {
	public static void main(String[] args) throws IOException {

		System.out.println("start: " + new Date());
		Properties prop = DtUtil.getConfiguration();
		int level = Integer.parseInt(prop.getProperty("level"));
		LinkedList<Question> questions = new LinkedList<Question>();
		// questions.addAll(DtUtil.generateNGramQuestions(1)); // bigram
		questions.addAll(DtUtil.generateNGramQuestions(2)); // trigram
		DecisionTree tree = new DecisionTree(questions);
		tree.growDT(level);

		String corpusFilePath = prop.getProperty("corpusFilePath");
		int historySize = Integer.parseInt(DtUtil.getConfiguration()
				.getProperty("historySize"));
		ArrayList<String> corpusData = DtUtil.getCorpusData(corpusFilePath);
		DataSetEntryIterator it = new DataSetEntryIterator(corpusData,
				historySize);
		double aveLogLik = tree.calcAverageLogLikelihood(it);
		double perplexity = tree.calcPerplexity(aveLogLik);
		System.out.println("on training data");
		System.out.println("level to grow: " + level);
		System.out.println("Average log likelihood: " + aveLogLik);
		System.out.println("Perplexity: " + perplexity);

		// Set<NGramQuestion> questions = DtUtil.generateNGramQuestions(3);
		// Set<NGramQuestion> questions = new HashSet<NGramQuestion>();
		// NGramQuestion q1 = new NGramQuestion(new String[] { "DT" });
		// NGramQuestion q2 = new NGramQuestion(new String[] { "DT" });
		// System.out.println(q1.equals(q2));
		// System.out.println(q1.hashCode());
		// System.out.println(q2.hashCode());
		// questions.add(q1);
		// questions.add(q2);

		// System.out.println("questions size: " + questions.size());
		// for (Question q : questions) {
		// System.out.println(q);
		// }
		System.out.println("end: " + new Date());

	}
}
