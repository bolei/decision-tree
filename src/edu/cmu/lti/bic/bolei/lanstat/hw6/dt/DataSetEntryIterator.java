package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class DataSetEntryIterator implements Iterator<DataSetEntry> {

	private static int HISTORY_SIZE;

	private ArrayList<String> trainingData = new ArrayList<String>();

	private int pointer;

	public DataSetEntryIterator(ArrayList<String> data, int historySize)
			throws IOException {
		HISTORY_SIZE = historySize;
		trainingData = data;
	}

	@Override
	public boolean hasNext() {
		return pointer < trainingData.size() && pointer >= 0 ? true : false;
	}

	@Override
	public DataSetEntry next() {
		int histBegin = Math.max(pointer - HISTORY_SIZE, 0);
		int histEnd = pointer;
		pointer++;
		return new DataSetEntry(histBegin, histEnd, trainingData);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
