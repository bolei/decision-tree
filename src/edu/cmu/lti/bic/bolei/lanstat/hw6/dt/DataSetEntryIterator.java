package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DataSetEntryIterator implements Iterator<DataSetEntry> {

	private BufferedReader brIn;
	private List<String> buff = new LinkedList<String>();
	private List<String> history = new LinkedList<String>();
	private String tag;

	public DataSetEntryIterator(String corpusFilePath) throws IOException {
		brIn = new BufferedReader(new FileReader(corpusFilePath));
		loadBuffer();
	}

	@Override
	public boolean hasNext() {
		if (buff.size() == 0) {
			loadBuffer();
		}
		return buff.isEmpty() ? false : true;
	}

	@Override
	public DataSetEntry next() {
		String tok = buff.remove(0);
		while (history.size() >= DecisionTreeNode.HISTORY_SIZE) {
			history.remove(0);
		}
		if (tag != null) {
			history.add(tag);
		}
		tag = tok;
		return DataSetEntry.getInstance(DtUtil.copyStringList(history),
				new String(tag));
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	private void loadBuffer() {
		String line;
		try {
			line = brIn.readLine();
			if (line != null) {
				Collections.addAll(buff, line.split("\\s+"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
