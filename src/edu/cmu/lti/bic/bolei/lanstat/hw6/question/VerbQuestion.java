package edu.cmu.lti.bic.bolei.lanstat.hw6.question;

/**
 * Is verb in the history?
 * 
 * @author bolei
 * 
 */
public class VerbQuestion extends HistoryContainsQuestion {

	@Override
	protected boolean isTargetType(String oneHistory) {
		return oneHistory.startsWith("VB") ? true : false;
	}

	@Override
	protected String questionTarget() {
		return "verb";
	}

}
