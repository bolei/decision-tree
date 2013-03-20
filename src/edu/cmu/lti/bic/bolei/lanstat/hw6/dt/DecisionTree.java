package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.cmu.lti.bic.bolei.lanstat.hw6.DtUtil;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.Question;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.QuestionScore;

public class DecisionTree {
	private DecisionTreeNode root;
	private LinkedList<DataSetEntry> memDataSet = new LinkedList<DataSetEntry>();
	private List<? extends Question> questions = new LinkedList<Question>();

	public DecisionTree(LinkedList<Question> questions) throws IOException {
		Properties prop = DtUtil.getConfiguration();
		String corpusFilePath = prop.getProperty("corpusFilePath");
		int historySize = Integer.parseInt(DtUtil.getConfiguration()
				.getProperty("historySize"));

		ArrayList<String> trainingData = DtUtil.getCorpusData(corpusFilePath);

		DataSetEntryIterator dataSetIterator = new DataSetEntryIterator(
				trainingData, historySize);
		while (dataSetIterator.hasNext()) {
			memDataSet.add(dataSetIterator.next());
		}
		acceptQuestions(questions);
		root = new DecisionTreeNode(memDataSet, new int[] {}, new boolean[] {});
		root.createLanguageModel();
	}

	public DecisionTreeNode getRoot() {
		return root;
	}

	public void growDT(int maxLevel) {
		if (maxLevel > questions.size() + 1) {
			System.err.println("maxLevel is larger than allowed ("
					+ (questions.size() + 1) + ")");
			return;
		}
		int levelCount = 1;

		// find best question, grow the node
		growDTNode(root, levelCount, maxLevel);
	}

	public double getProbability(DataSetEntry entry) {
		return getProbability(root, entry);
	}

	public double calcAverageLogLikelihood(DataSetEntryIterator it) {
		double avgLogLik = 0f;
		int count = 0;
		while (it.hasNext()) {
			avgLogLik += DtUtil.logBase2(getProbability(it.next()));
			count += 1;
		}
		return avgLogLik / count;
	}

	public double calcPerplexity(DataSetEntryIterator it) {
		double avgLogLik = calcAverageLogLikelihood(it);
		return Math.pow(2, avgLogLik * (-1));
	}

	public double calcPerplexity(double avgLogLik) {
		return Math.pow(2, avgLogLik * (-1));
	}

	private void acceptQuestions(List<? extends Question> questions) {
		int questionSize = questions.size();
		this.questions = questions;
		// long count = 0;
		// long temp = 0, window = 100000;
		for (DataSetEntry entry : memDataSet) {
			// if (count / window != temp) {
			// System.out.println(count / window);
			// temp = count / window;
			// }
			entry.initAnswers(new boolean[questionSize]);
			Iterator<? extends Question> it = questions.iterator();
			int questionIndex = 0;
			while (it.hasNext()) {
				// count++;
				entry.setAnswer(questionIndex, it.next().giveAnswer(entry));
				questionIndex++;
			}
		}
		System.out.println("accepted questions");
	}

	private double getProbability(DecisionTreeNode node, DataSetEntry entry) {
		if (node.getQuestionIndex() < 0) {
			return node.getProbability(entry.getToken());
		}
		Question q = questions.get(node.getQuestionIndex());
		if (q.giveAnswer(entry) == true) {
			return getProbability(node.getYesChild(), entry);
		} else {
			return getProbability(node.getNoChild(), entry);
		}
	}

	private void growDTNode(DecisionTreeNode node, int levelCount, int maxLevel) {
		if (levelCount >= maxLevel) {
			return;
		}
		if (questions == null || questions.isEmpty()) {
			throw new NullPointerException(
					"question list is empty or null at level " + levelCount);
		}
		int bestQuestionIndex = 0;
		double bestScore = 0, tempScore = 0;
		for (int i = 0; i < questions.size(); i++) {
			if (DtUtil.valInArray(node.getHistoryQuestionIds(), i)) {
				// asked question, move to next question
				continue;
			}
			System.out.println(i);
			tempScore = node.getMutalInformationForQuestion(i);
			if (tempScore > bestScore) {
				bestScore = tempScore;
				bestQuestionIndex = i;
			}
		}
		boolean growRst = node.growDT(bestQuestionIndex);
		if (growRst == false) { // grow not successful
			return;
		}
		growDTNode(node.getYesChild(), levelCount + 1, maxLevel);
		growDTNode(node.getNoChild(), levelCount + 1, maxLevel);
	}

	public List<QuestionScore> rankQuestions() {
		System.out.println("question size=" + questions.size());
		List<QuestionScore> result = new LinkedList<QuestionScore>();
		for (int i = 0; i < questions.size(); i++) {
			System.out.println(i);
			double score = root.getMutalInformationForQuestion(i);
			result.add(new QuestionScore(questions.get(i), score));
		}
		Collections.sort(result, Collections.<QuestionScore> reverseOrder());
		return result;
	}

}
