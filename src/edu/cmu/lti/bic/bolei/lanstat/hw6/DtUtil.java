package edu.cmu.lti.bic.bolei.lanstat.hw6;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.cmu.lti.bic.bolei.lanstat.hw6.question.NGramQuestion;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.Question;

public class DtUtil {

	private static Properties prop;

	public static LinkedList<String> copyStringList(List<String> origin) {
		LinkedList<String> copy = new LinkedList<String>();
		for (String str : origin) {
			copy.add(new String(str));
		}
		return copy;
	}

	public static Properties getConfiguration() throws IOException {
		if (prop == null) {
			prop = new Properties();
			prop.load(new FileInputStream("resources/config.properties"));
		}
		return prop;
	}

	public static List<Question> generateNGramQuestions(int n)
			throws IOException {
		List<Question> questions = new LinkedList<Question>();
		String[] tokens = getConfiguration().getProperty("tokens").split("#");
		int[] counter = new int[n];
		Arrays.fill(counter, 0);
		int scale = tokens.length;
		for (int i = 0; i < Math.pow(scale, n); i++) {
			counter = counterPlusOne(counter, scale);
			String[] ngramHistory = new String[n];
			for (int j = 0; j < n; j++) {
				ngramHistory[j] = tokens[counter[j]];
			}
			questions.add(new NGramQuestion(ngramHistory));
		}
		return questions;
	}

	private static int[] counterPlusOne(int[] counter, int scale) {
		int len = counter.length;
		int i = len - 1;
		counter[i] = (counter[i] + 1) % scale;
		while (counter[i] == 0 && i > 0) {
			i--;
			counter[i] = (counter[i] + 1) % scale;
		}
		return counter;
	}
}
