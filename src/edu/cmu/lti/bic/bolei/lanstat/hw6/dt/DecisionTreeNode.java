package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import edu.cmu.lti.bic.bolei.lanstat.hw6.DtUtil;

public class DecisionTreeNode {

	private int[] historyQuestionIds;
	private boolean[] historyQuestionAnswers;
	private int questionIndex;

	/**
	 * Language model created from dataset file
	 */
	private HashMap<String, Double> langModel;
	/**
	 * (h, t) pairs
	 */
	private List<DataSetEntry> memDataSet;
	private String id;
	private DecisionTreeNode yesChild, noChild;

	public DecisionTreeNode(List<DataSetEntry> dataset, int[] historyQids,
			boolean[] historyAns) {
		yesChild = null;
		noChild = null;
		id = UUID.randomUUID().toString();
		memDataSet = dataset;
		id = UUID.randomUUID().toString();
		historyQuestionIds = historyQids;
		historyQuestionAnswers = historyAns;
		questionIndex = -1;
	}

	public boolean[] getHistoryQuestionAnswers() {
		return historyQuestionAnswers;
	}

	public int[] getHistoryQuestionIds() {
		return historyQuestionIds;
	}

	public DecisionTreeNode getYesChild() {
		return yesChild;
	}

	public DecisionTreeNode getNoChild() {
		return noChild;
	}

	public double getNodeEntropy() {
		double entropy = 0f;
		for (Entry<String, Double> entry : langModel.entrySet()) {
			entropy += entry.getValue() * DtUtil.logBase2(1 / entry.getValue());
		}
		return entropy;
	}

	public double getMutalInformation() {
		int yesDataSize = yesChild.getDataSize();
		int noDataSize = noChild.getDataSize();
		double yesProb = ((double) yesDataSize) / (yesDataSize + noDataSize);
		double noProb = ((double) noDataSize) / (yesDataSize + noDataSize);
		return getNodeEntropy() - yesProb * yesChild.getNodeEntropy() - noProb
				* noChild.getNodeEntropy();
	}

	public double getProbability(String token) {
		return langModel.get(token);
	}

	/*
	 * Preorder visit
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("node id = " + id + "\n");
		if (questionIndex != -1) {
			sb.append("node question Id: " + questionIndex + "\n");
		}
		sb.append("lang model:" + "\n");
		sb.append(getLanguageModelString());
		sb.append("root entropy = " + getNodeEntropy() + "\n");
		if (yesChild != null && noChild != null) {
			sb.append("Mutual Information = " + getMutalInformation() + "\n");
		}
		if (yesChild != null) {
			sb.append(yesChild.toString());
		}
		if (noChild != null) {
			sb.append(noChild.toString());
		}
		return sb.toString();
	}

	void createLanguageModel() {
		langModel = new HashMap<String, Double>();

		for (DataSetEntry entry : memDataSet) {
			if (entry.meetMask(historyQuestionIds, historyQuestionAnswers)) {
				addTokenToLanguageModel(entry.getToken());
			}
		}
		for (Entry<String, Double> entry : langModel.entrySet()) {
			entry.setValue(entry.getValue() / ((double) memDataSet.size()));
		}
	}

	double getMutalInformationForQuestion(int questionsIndex) {
		if (growDT(questionsIndex) == false) {
			return 0;
		}
		return getMutalInformation();
	}

	boolean growDT(int questionsIndex) {
		if (questionsIndex < 0) {
			return false;
		}
		int[] childQuestionHistory = createQuestionHistoryForChild(questionsIndex);
		boolean[] yesChildAnswersHistory = createAnswerHistoryForChild(true);
		boolean[] noChildAnswersHistory = createAnswerHistoryForChild(false);

		yesChild = new DecisionTreeNode(memDataSet, childQuestionHistory,
				yesChildAnswersHistory);
		noChild = new DecisionTreeNode(memDataSet, childQuestionHistory,
				noChildAnswersHistory);
		this.questionIndex = questionsIndex;
		yesChild.createLanguageModel();
		noChild.createLanguageModel();
		if (yesChild.langModel.size() == 0 || noChild.langModel.size() == 0) {
			rollBackGrow();
			return false;
		}
		return true;
	}

	int getQuestionIndex() {
		return questionIndex;
	}

	void setQuestionIndex(int index) {
		questionIndex = index;
	}

	private int[] createQuestionHistoryForChild(int questionIndex) {
		int[] childQuestionHistory = Arrays.copyOf(historyQuestionIds,
				historyQuestionIds.length + 1);
		childQuestionHistory[historyQuestionIds.length] = questionIndex;
		return childQuestionHistory;
	}

	private boolean[] createAnswerHistoryForChild(boolean childAnswer) {
		boolean[] answerHistory = Arrays.copyOf(historyQuestionAnswers,
				historyQuestionAnswers.length + 1);
		answerHistory[historyQuestionAnswers.length] = childAnswer;
		return answerHistory;
	}

	private int getDataSize() {
		if (historyQuestionAnswers == null
				|| historyQuestionAnswers.length == 0
				|| historyQuestionIds == null || historyQuestionIds.length == 0) {
			return memDataSet.size();
		}
		int size = 0;
		for (DataSetEntry entry : memDataSet) {
			if (entry.meetMask(historyQuestionIds, historyQuestionAnswers)) {
				size++;
			}
		}
		return size;
	}

	private String getLanguageModelString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Double> entry : langModel.entrySet()) {
			sb.append(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		return sb.toString();
	}

	private void addTokenToLanguageModel(String tok) {
		if (langModel.containsKey(tok)) {
			langModel.put(tok, langModel.get(tok) + 1);
		} else {
			langModel.put(tok, 1d);
		}
	}

	private void rollBackGrow() {
		yesChild = null;
		noChild = null;
		questionIndex = -1;
	}

}
