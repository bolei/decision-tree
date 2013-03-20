package edu.cmu.lti.bic.bolei.lanstat.hw6;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntry;
import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntryIterator;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.ContainsNWordsQuestion;
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
		Set<NGramQuestion> questions = new HashSet<NGramQuestion>();
		String corpusFilePath = getConfiguration()
				.getProperty("corpusFilePath");
		ArrayList<String> trainingData = getCorpusData(corpusFilePath);
		DataSetEntryIterator it = new DataSetEntryIterator(trainingData, n);
		DataSetEntry entry;
		while (it.hasNext()) {
			entry = it.next();
			if (entry.getHistoryLength() < n) {
				continue;
			}
			questions.add(new NGramQuestion(entry.getHistory()));
		}
		return new LinkedList<Question>(questions);
	}

	public static List<Question> generateContainsNWordsQuestions(int n)
			throws IOException {
		LinkedList<Question> questions = new LinkedList<Question>();
		String[] tokens = getConfiguration().getProperty("tokens").split("#");
		int[] counter = new int[n];
		Arrays.fill(counter, 0);
		int scale = tokens.length;
		for (int i = 0; i < Math.pow(scale, n); i++) {
			String[] nTokens = new String[n];
			for (int j = 0; j < n; j++) {
				nTokens[j] = tokens[counter[j]];
			}
			questions.add(new ContainsNWordsQuestion(nTokens));
			counterPlusOne(counter, scale);
		}
		return questions;
	}

	public static double logBase2(double val) {
		return Math.log(val) / Math.log(2);
	}

	public static boolean valInArray(int[] array, int value) {
		for (int v : array) {
			if (v == value) {
				return true;
			}
		}
		return false;
	}

	public static boolean valInArray(boolean[] array, boolean value) {
		for (boolean v : array) {
			if (v == value) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<String> getCorpusData(String corpusFilePath)
			throws IOException {
		LinkedList<String> trainingData = new LinkedList<String>();
		BufferedReader brIn = new BufferedReader(new FileReader(corpusFilePath));

		String line;
		while ((line = brIn.readLine()) != null) {
			Collections.addAll(trainingData, line.split("\\s+"));
		}
		return new ArrayList<String>(trainingData);
	}

	private static void counterPlusOne(int[] counter, int scale) {
		int len = counter.length;
		int i = len - 1;
		counter[i] = (counter[i] + 1) % scale;
		while (counter[i] == 0 && i > 0) {
			i--;
			counter[i] = (counter[i] + 1) % scale;
		}
	}
}
