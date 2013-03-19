package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.util.ArrayList;

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

	// public static DataSetEntry parseFileLine(String line) {
	// DataSetEntry entry = new DataSetEntry();
	// String[] items = line.split("\\s+");
	// Collections.addAll(entry.history, items[0].split("#"));
	// entry.token = items[1];
	// return entry;
	// }
	//
	// public static DataSetEntry getInstance(List<String> history, String
	// token) {
	// DataSetEntry entry = new DataSetEntry();
	// entry.history = history;
	// entry.token = token;
	// return entry;
	// }

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
			if (answers[questionIds[i]] != answers[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return getHistoryString() + "\t" + getToken();
	}

	private String getHistoryString() {
		StringBuilder sb = new StringBuilder();
		for (int i = historyBeginPos; i < historyEndPos; i++) {
			sb.append(trainingData.get(i) + "#");
		}
		return sb.toString();
	}
}
