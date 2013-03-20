package edu.cmu.lti.bic.bolei.lanstat.hw6;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DecisionTree;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.Question;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.QuestionScore;

public class Problem3 {
	public static void main(String[] args) throws IOException {
		System.out.println("==============problem 3 ================");
		// generating questions
		List<Question> questions = new LinkedList<Question>();
		questions.addAll(DtUtil.generateNGramQuestions(1));
		questions.addAll(DtUtil.generateNGramQuestions(2));
		questions.addAll(DtUtil.generateContainsNWordsQuestions(1));
		questions.addAll(DtUtil.generateContainsNWordsQuestions(2));

		// generate tree
		DecisionTree tree = new DecisionTree(
				new LinkedList<Question>(questions));

		// rank questions
		List<QuestionScore> questionScoreList = tree.rankQuestions();

		// display
		for (int i = 0; i < 100; i++) {
			System.out.println(questionScoreList.get(i));
		}

		double average = 0d;
		for (QuestionScore qs : questionScoreList) {
			average += qs.getScore();
		}
		average = average / questionScoreList.size();
		System.out.println("average score is: " + average);

	}
}
