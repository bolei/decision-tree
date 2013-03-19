package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DataSetEntryIterator implements Iterator<DataSetEntry> {

	private static int HISTORY_SIZE;

	private List<String> trainingData = new LinkedList<String>();
	private int pointer;

	public DataSetEntryIterator(List<String> data, int historySize)
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
		System.out.println(pointer);
		int histBegin = Math.max(pointer - HISTORY_SIZE, 0);
		int histEnd = pointer;
		pointer++;
		return new DataSetEntry(histBegin, histEnd, new ArrayList<String>(
				trainingData));
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
