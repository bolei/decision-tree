package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import edu.cmu.lti.bic.bolei.lanstat.hw6.question.Question;

public class DecisionTreeNode {
	public static final int HISTORY_SIZE = 20;
	private String outFolder;

	/**
	 * Language model created from dataset file
	 */
	private HashMap<String, Float> langModel;
	/**
	 * (h, t) pairs
	 */
	private LinkedList<DataSetEntry> memDataSet;
	private File dataSetFile;
	private long dataSetSize = 0;
	private String id;
	private DecisionTreeNode yesChild, noChild;

	private DecisionTreeNode(String dataSetFolder) {
		yesChild = null;
		noChild = null;
		id = UUID.randomUUID().toString();
		outFolder = dataSetFolder;
		memDataSet = new LinkedList<DataSetEntry>();
	}

	public DecisionTreeNode getYesChild() {
		return yesChild;
	}

	public DecisionTreeNode getNoChild() {
		return noChild;
	}

	public void growDT(Question question) {
		yesChild = new DecisionTreeNode(outFolder);
		noChild = new DecisionTreeNode(outFolder);
		DataSetEntry entry;
		while (!memDataSet.isEmpty()) {
			entry = memDataSet.removeFirst();
			if (question.giveAnswer(entry) == true) {// yes
				yesChild.appendDataSetEntry(entry);
			} else {// no
				noChild.appendDataSetEntry(entry);
			}
		}
		yesChild.createLanguageModel();
		noChild.createLanguageModel();
	}

	public static DecisionTreeNode initializeTree() throws IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("resources/config.properties"));
		String corpusFilePath = prop.getProperty("corpusFilePath");
		String dataSetFolder = prop.getProperty("dataSetFolder");

		DecisionTreeNode dtNode = new DecisionTreeNode(dataSetFolder);

		DataSetEntryIterator it = new DataSetEntryIterator(corpusFilePath);
		dtNode.appendDataSetEntryBatch(it, true);
		dtNode.createLanguageModel();
		return dtNode;
	}

	public float getNodeEntropy() {
		float entropy = 0f;
		for (Entry<String, Float> entry : langModel.entrySet()) {
			entropy += entry.getValue()
					* (Math.log(1 / entry.getValue()) / Math.log(2));
		}
		return entropy;
	}

	public float getMutalInformation() {
		float yesProb = ((float) yesChild.dataSetSize) / ((float) dataSetSize);
		float noProb = ((float) noChild.dataSetSize) / ((float) dataSetSize);
		return getNodeEntropy() - yesProb * yesChild.getNodeEntropy() - noProb
				* noChild.getNodeEntropy();
	}

	public void dumpDataSet() throws IOException {
		if (dataSetFile == null) {
			dataSetFile = new File(outFolder + "/" + id);
			dataSetFile.createNewFile();
		}
		Iterator<DataSetEntry> it = memDataSet.iterator();
		appendDataSetEntryBatch(it, false);
	}

	/*
	 * Preorder visit
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("node id = " + id + "\n");
		sb.append("lang model:" + "\n");
		sb.append(getLanguageModelString());
		sb.append("root entropy = " + getNodeEntropy() + "\n");
		if (yesChild != null && noChild != null) {
			sb.append("Mutual Information = " + getMutalInformation() + "\n");
		}
		if (yesChild != null) {
			sb.append(yesChild.toString());
		}
		if (noChild != null) {
			sb.append(noChild.toString());
		}
		return sb.toString();
	}

	private String getLanguageModelString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Float> entry : langModel.entrySet()) {
			sb.append(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		return sb.toString();
	}

	private void createLanguageModel() {
		langModel = new HashMap<String, Float>();
		for (DataSetEntry entry : memDataSet) {
			addTokenToLanguageModel(entry.getToken());
		}
		for (Entry<String, Float> entry : langModel.entrySet()) {
			entry.setValue(entry.getValue() / ((float) dataSetSize));
		}
	}

	private void addTokenToLanguageModel(String tok) {
		if (langModel.containsKey(tok)) {
			langModel.put(tok, langModel.get(tok) + 1);
		} else {
			langModel.put(tok, 1f);
		}
	}

	private void appendDataSetEntryBatch(Iterator<DataSetEntry> it,
			boolean inMemory) throws IOException {
		if (inMemory == false) {
			BufferedWriter brOut = new BufferedWriter(new FileWriter(
					dataSetFile));
			while (it.hasNext()) {
				brOut.write(it.next().toString() + "\n");
				dataSetSize++;
			}
			brOut.close();
		} else {
			while (it.hasNext()) {
				memDataSet.add(it.next());
			}
			dataSetSize = memDataSet.size();
		}
	}

	private void appendDataSetEntry(DataSetEntry entry) {
		memDataSet.add(entry);
		dataSetSize++;
	}

}
