package edu.cmu.lti.bic.bolei.lanstat.hw6.question;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntry;

/**
 * Is verb in the history?
 * 
 * @author bolei
 * 
 */
public class VerbQuestion implements Question {

	@Override
	public boolean giveAnswer(final DataSetEntry entry) {
		for (String his : entry.getHistory()) {
			if (his.startsWith("VB")) {
				return true;
			}
		}
		return false;
	}
}
