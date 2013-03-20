package edu.cmu.lti.bic.bolei.lanstat.hw6.question;

public class QuestionScore implements Comparable<QuestionScore> {
	private double score;
	private Question question;

	public QuestionScore(Question q, double scr) {
		score = scr;
		question = q;
	}

	public Question getQuestion() {
		return question;
	}

	public double getScore() {
		return score;
	}

	@Override
	public int compareTo(QuestionScore o) {
		if (score < o.score) {
			return -1;
		} else if (score == o.score) {
			return 0;
		} else
			return 1;
	}

}
