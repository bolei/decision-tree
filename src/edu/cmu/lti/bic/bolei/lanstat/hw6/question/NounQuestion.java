package edu.cmu.lti.bic.bolei.lanstat.hw6.question;

public class NounQuestion extends HistoryContainsQuestion {

	@Override
	protected boolean isTargetType(String oneHistory) {
		return oneHistory.startsWith("NN") ? true : false;
	}

	@Override
	protected String questionTarget() {
		return "noun";
	}

}
