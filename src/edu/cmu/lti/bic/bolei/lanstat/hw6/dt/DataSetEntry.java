package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.util.ArrayList;
import java.util.Arrays;

public class DataSetEntry {
	private int historyBeginPos = 0; // begin position of history, inclusive
	private int historyEndPos = 0; // end position of history, exclusive
	private ArrayList<String> trainingData;
	private boolean[] answers;

	public DataSetEntry(int histBegin, int histEnd,
			ArrayList<String> trainingData) {
		this.historyBeginPos = histBegin;
		this.historyEndPos = histEnd;
		this.trainingData = trainingData;
	}

	public String[] getHistory() {
		return trainingData.subList(historyBeginPos, historyEndPos).toArray(
				new String[] {});
	}

	public String getToken() {
		return trainingData.get(historyEndPos);
	}

	public int getHistoryLength() {
		return historyEndPos - historyBeginPos;
	}

	public void initAnswers(boolean[] answers) {
		this.answers = answers;
	}

	public void setAnswer(int questionIndex, boolean answer) {
		answers[questionIndex] = answer;
	}

	public boolean getAnswer(int questionIndex) {
		return answers[questionIndex];
	}

	public boolean meetMask(int[] questionIds, boolean[] answers) {
		if (questionIds == null || questionIds.length == 0 || answers == null
				|| answers.length == 0) {
			return true;
		}
		for (int i = 0; i < questionIds.length; i++) {
			if (this.answers[questionIds[i]] != answers[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return getHistoryString() + "\t" + getToken() + "\t"
				+ Arrays.toString(answers);
	}

	private String getHistoryString() {
		StringBuilder sb = new StringBuilder();
		for (int i = historyBeginPos; i < historyEndPos; i++) {
			sb.append(trainingData.get(i) + "#");
		}
		return sb.toString();
	}
}
