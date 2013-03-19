package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.cmu.lti.bic.bolei.lanstat.hw6.DtUtil;

public class DataSetEntry {
	private List<String> history = new LinkedList<String>();
	private String token;
	private boolean[] answers;

	private DataSetEntry() {
	}

	public List<String> getHistory() {
		return history;
	}

	public String getToken() {
		return token;
	}

	public static DataSetEntry parseFileLine(String line) {
		DataSetEntry entry = new DataSetEntry();
		String[] items = line.split("\\s+");
		Collections.addAll(entry.history, items[0].split("#"));
		entry.token = items[1];
		return entry;
	}

	public static DataSetEntry getInstance(List<String> history, String token) {
		DataSetEntry entry = new DataSetEntry();
		entry.history = history;
		entry.token = token;
		return entry;
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
			if (answers[questionIds[i]] != answers[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return getHistoryString() + "\t" + token;
	}

	private String getHistoryString() {
		StringBuilder sb = new StringBuilder();
		for (String h : history) {
			sb.append(h + "#");
		}
		return sb.toString();
	}

	public DataSetEntry getCopy() {
		DataSetEntry entry = new DataSetEntry();
		entry.history = DtUtil.copyStringList(history);
		entry.token = new String(token);
		return entry;
	}

}
