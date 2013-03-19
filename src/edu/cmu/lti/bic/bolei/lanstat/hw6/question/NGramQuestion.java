package edu.cmu.lti.bic.bolei.lanstat.hw6.question;

import java.util.Arrays;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntry;

public class NGramQuestion implements Question {

	private String[] target;

	public NGramQuestion(String[] tgt) {
		target = tgt;
	}

	@Override
	public boolean giveAnswer(DataSetEntry entry) {
		int histLen = entry.getHistoryLength();
		if (histLen < target.length) {
			return false;
		}
		for (int i = 0; i < target.length; i++) {
			if (!target[target.length - 1 - i]
					.trim()
					.toLowerCase()
					.equals(entry.getHistory()[histLen - 1 - i].trim()
							.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "is NGram history: " + Arrays.toString(target);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NGramQuestion)) {
			return false;
		}
		NGramQuestion q = (NGramQuestion) obj;
		boolean eqs = Arrays.equals(this.target, q.target);

		return eqs;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for (String str : target) {
			hash = hash * 37 + str.hashCode();
		}
		return hash;
	}

}
