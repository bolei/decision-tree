package edu.cmu.lti.bic.bolei.lanstat.hw6.question;

import java.util.Arrays;
import java.util.List;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntry;

public class ContainsNWordsQuestion implements Question {

	private List<String> nTokens;

	public ContainsNWordsQuestion(String[] nTokens) {
		this.nTokens = Arrays.asList(nTokens);
	}

	@Override
	public boolean giveAnswer(DataSetEntry entry) {
		String[] history = entry.getHistory();
		for (String h : history) {
			if (nTokens.contains(h) == false) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "History contains " + nTokens;
	}

}
