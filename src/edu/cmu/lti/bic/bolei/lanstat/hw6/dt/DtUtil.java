package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

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
}
