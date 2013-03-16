package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.util.LinkedList;
import java.util.List;

public class DtUtil {
	public static LinkedList<String> copyStringList(List<String> origin) {
		LinkedList<String> copy = new LinkedList<String>();
		for (String str : origin) {
			copy.add(new String(str));
		}
		return copy;
	}
}
