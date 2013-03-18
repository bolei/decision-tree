package edu.cmu.lti.bic.bolei.lanstat.hw6;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntry;
import edu.cmu.lti.bic.bolei.lanstat.hw6.dt.DataSetEntryIterator;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.NGramQuestion;

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

	public static Set<NGramQuestion> generateNGramQuestions(int n)
			throws IOException {
		Set<NGramQuestion> questions = new HashSet<NGramQuestion>();
		String corpusFilePath = getConfiguration()
				.getProperty("corpusFilePath");
		DataSetEntryIterator it = new DataSetEntryIterator(corpusFilePath, n);
		DataSetEntry entry;
		while (it.hasNext()) {
			entry = it.next();
			if (entry.getHistory().size() < n) {
				continue;
			}
			questions.add(new NGramQuestion(entry.getHistory().toArray(
					new String[] {})));
		}
		return questions;
	}

}
