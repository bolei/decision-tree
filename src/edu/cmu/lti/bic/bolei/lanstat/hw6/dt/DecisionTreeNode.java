package edu.cmu.lti.bic.bolei.lanstat.hw6.dt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import edu.cmu.lti.bic.bolei.lanstat.hw6.DtUtil;
import edu.cmu.lti.bic.bolei.lanstat.hw6.question.Question;

public class DecisionTreeNode implements Serializable {
	private static final long serialVersionUID = -5810294510905722810L;

	private String outFolder;
	private Question question;

	/**
	 * Language model created from dataset file
	 */
	private HashMap<String, Double> langModel;
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

	public double getNodeEntropy() {
		double entropy = 0f;
		for (Entry<String, Double> entry : langModel.entrySet()) {
			entropy += entry.getValue() * logBase2(1 / entry.getValue());
		}
		return entropy;
	}

	public double getMutalInformation() {
		double yesProb = ((double) yesChild.dataSetSize)
				/ ((double) dataSetSize);
		double noProb = ((double) noChild.dataSetSize) / ((double) dataSetSize);
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

	public double getProbability(DataSetEntry entry) {
		if (yesChild == null || noChild == null || question == null) {
			return langModel.get(entry.getToken());
		}
		if (question.giveAnswer(entry) == true) {
			return yesChild.getProbability(entry);
		} else {
			return noChild.getProbability(entry);
		}
	}

	public double calcAverageLogLikelihood(DataSetEntryIterator it) {
		double avgLogLik = 0f;
		int count = 0;
		while (it.hasNext()) {
			avgLogLik += logBase2(getProbability(it.next()));
			count += 1;
		}
		return avgLogLik / count;
	}

	public double calcPerplexity(DataSetEntryIterator it) {
		double avgLogLik = calcAverageLogLikelihood(it);
		return Math.pow(2, avgLogLik * (-1));
	}

	public double calcPerplexity(double avgLogLik) {
		return Math.pow(2, avgLogLik * (-1));
	}

	/*
	 * Preorder visit
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("node id = " + id + "\n");
		if (question != null) {
			sb.append("node question: " + question.toString() + "\n");
		}
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

	public static DecisionTreeNode initializeTree() throws IOException {
		Properties prop = DtUtil.getConfiguration();
		String corpusFilePath = prop.getProperty("corpusFilePath");
		String dataSetFolder = prop.getProperty("dataSetFolder");
		int historySize = Integer.parseInt(DtUtil.getConfiguration()
				.getProperty("historySize"));

		DecisionTreeNode dtNode = new DecisionTreeNode(dataSetFolder);

		DataSetEntryIterator it = new DataSetEntryIterator(corpusFilePath,
				historySize);
		dtNode.appendDataSetEntryBatch(it, true);
		dtNode.createLanguageModel();
		return dtNode;
	}

	public static DecisionTreeNode growDT(List<? extends Question> questions,
			int maxLevel) throws IOException {
		if (maxLevel > questions.size() + 1) {
			System.out.println("maxLevel is larger than allowed ("
					+ (questions.size() + 1) + ")");
			return null;
		}
		DecisionTreeNode root = initializeTree();
		int levelCount = 1;

		// find best question, grow the node
		growDTNode(root, questions, levelCount, maxLevel);
		return root;
	}

	private static void growDTNode(DecisionTreeNode node,
			List<? extends Question> questions, int levelCount, int maxLevel) {
		if (levelCount >= maxLevel) {
			return;
		}
		if (questions == null || questions.isEmpty()) {
			throw new NullPointerException(
					"question list is empty or null at level " + levelCount);
		}
		Question bestQuestion = null;
		double bestScore = 0, tempScore = 0;
		int count = 0;
		System.out.println("question size: " + questions.size());
		for (Question q : questions) {
			System.out.println("count=" + count++);
			tempScore = node.tryGrowDT(q);
			if (tempScore > bestScore) {
				bestScore = tempScore;
				bestQuestion = q;
			}
		}
		boolean growRst = node.growDT(bestQuestion);
		if (growRst == false) { // grow not successful
			return;
		}
		questions.remove(bestQuestion);
		growDTNode(node.yesChild, new LinkedList<Question>(questions),
				levelCount + 1, maxLevel);
		growDTNode(node.noChild, new LinkedList<Question>(questions),
				levelCount + 1, maxLevel);
	}

	private boolean growDT(Question question) {
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
		if (yesChild.memDataSet.size() == 0 || noChild.memDataSet.size() == 0) {
			rollBackGrow();
			return false;
		}
		this.question = question;
		yesChild.createLanguageModel();
		noChild.createLanguageModel();
		return true;
	}

	private double tryGrowDT(Question question) {
		if (growDT(question) == true) {
			double mutInfo = getMutalInformation();
			rollBackGrow();
			return mutInfo;
		} else {
			// grow failed, already rolled back
			return 0;
		}

	}

	private double logBase2(double val) {
		return Math.log(val) / Math.log(2);
	}

	private String getLanguageModelString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Double> entry : langModel.entrySet()) {
			sb.append(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		return sb.toString();
	}

	private void createLanguageModel() {
		langModel = new HashMap<String, Double>();
		for (DataSetEntry entry : memDataSet) {
			addTokenToLanguageModel(entry.getToken());
		}
		for (Entry<String, Double> entry : langModel.entrySet()) {
			entry.setValue(entry.getValue() / ((double) dataSetSize));
		}
	}

	private void addTokenToLanguageModel(String tok) {
		if (langModel.containsKey(tok)) {
			langModel.put(tok, langModel.get(tok) + 1);
		} else {
			langModel.put(tok, 1d);
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

	private void rollBackGrow() {
		memDataSet.addAll(yesChild.memDataSet);
		memDataSet.addAll(noChild.memDataSet);
		yesChild = null;
		noChild = null;
		question = null;
	}

}
