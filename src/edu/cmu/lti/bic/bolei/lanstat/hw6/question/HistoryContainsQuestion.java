package edu.cmu.lti.bic.bolei.lanstat.hw6.question;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntry;

public abstract class HistoryContainsQuestion implements Question {

	@Override
	public boolean giveAnswer(DataSetEntry entry) {
		for (String his : entry.getHistory()) {
			if (isTargetType(his)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Is " + questionTarget() + " in the history?";
	}

	protected abstract boolean isTargetType(String oneHistory);

	protected abstract String questionTarget();
}
