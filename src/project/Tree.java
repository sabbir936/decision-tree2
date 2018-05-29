package project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Tree {
	public int [][] data;
	public int [] decisionSet;
	public int [] numberOfAttributes;
	public String [] [] attributeList;
	public String [] variable;
	public ArrayList <Node> decisionNodes;
	public ArrayList <Integer> trainData, testData;
	
	public int defaultDecision;
	public Node root = null;
	public double accuracy;
	
	public Tree (Data dataset) {
		this.data = dataset.data;
		this.decisionSet = dataset.decisionSet;
		this.numberOfAttributes = dataset.numberOfAttributes;
		this.attributeList = dataset.attributeList;
		this.variable = dataset.variableName;
		
		trainAndTestDataGenerate();
		findRootAndBuildTree();
		decisionNodes = new ArrayList<Node>();
		createDecisionSet(root);
		
		//makeDecision(new int[] {1, 2, 0, 1, 2, 0});
		accuracyTesting();
		//makeDecision(new int[] {1, 1, 1, 1, 1, 1, 1, 1});
		//getDecisionList();
		//System.out.println(this.defaultDecision);
		//System.out.println("Accuracy: " + ((int)(this.accuracy*100)/(double)100) + "%");
	}

	private void findRootAndBuildTree() {
		ArrayList<Integer> availableColumns = new ArrayList<Integer>();
		for (int i=0; i<variable.length; i++) {
			availableColumns.add(i);
		}
		
		//The following three lines will not be needed for train data set tree construction
		ArrayList<Integer> availableRows = new ArrayList<Integer>();
		for (int i=0; i<decisionSet.length; i++) {
			availableRows.add(i);
		}
		
		//Variable temp = this.findMaxInformationGain (availableRows, availableColumns);
		
		Variable temp = this.findMaxInformationGain (this.trainData, availableColumns);
		root = new Node(temp, null);
		
		availableColumns.remove(root.variable.columnNumber);
		makeTree (root, availableColumns);
	}
	
	private Variable findMaxInformationGain (ArrayList<Integer> availableRows, ArrayList<Integer> availableColumns) {
		Variable maxInfoGain = null;
		for (int i=0; i<availableColumns.size(); i++) {
			int index = availableColumns.get(i);
			Variable temp = new Variable(index, variable[index], numberOfAttributes[index], attributeList[index], availableRows);
			
			if (maxInfoGain == null) maxInfoGain = temp;
			else if (maxInfoGain.informationGain < temp.informationGain) {
				maxInfoGain = temp;
			}
		}
		return maxInfoGain;
	}
	
	private void makeTree (Node currentNode, ArrayList<Integer> availableColumns) {
		if (currentNode.variable == null) return;
		
		if (currentNode.variable.avgChildEntropy == 0.00) {
			for (int i=0; i<currentNode.variable.numberOfAttributes; i++) {
				//if (currentNode.variable.attributes[i].availableRows.isEmpty()) continue;
				Attribute newAttribute = currentNode.variable.attributes[i];
				Node temp = new Node (newAttribute);
				currentNode.addChild(temp);
			}
			return ;
		}
		
		if (availableColumns.size() == 0) {
			for (int i=0; i<currentNode.variable.numberOfAttributes; i++) {
				Attribute newAttribute = currentNode.variable.attributes[i];
				Node temp = new Node (newAttribute);
				currentNode.addChild(temp);
			}
			return;
		}
		
		for (int i=0; i<currentNode.variable.numberOfAttributes; i++) {
			
			ArrayList <Integer> availableRows = currentNode.variable.dataRows[i];
			Attribute newAttribute = currentNode.variable.attributes[i];
			
			if (currentNode.variable.attributes[i].entropy == 0.00) {
				Node temp = new Node (newAttribute);
				currentNode.addChild(temp);
				continue;
			}
			
			Node temp = new Node (findMaxInformationGain(availableRows, availableColumns), newAttribute);
			currentNode.addChild(temp);
			
			ArrayList <Integer> updatedAvailableColumns = new ArrayList<Integer>();
			updatedAvailableColumns.addAll(availableColumns);
			
			int index = updatedAvailableColumns.indexOf(temp.variable.columnNumber);
			updatedAvailableColumns.remove(index);
			
			makeTree (temp, updatedAvailableColumns);
		}
		
	}
	
	/*public void traverse (Node myNode) {
		System.out.println(myNode);
		
		if (myNode.siblingNode != null) traverse (myNode.siblingNode);
		if (myNode.childNode != null) traverse (myNode.childNode);
	}*/
	
	public void createDecisionSet (Node currentNode) {
		if (currentNode.isLeaf) decisionNodes.add(currentNode);
		
		if (currentNode.siblingNode != null) createDecisionSet (currentNode.siblingNode);
		if (currentNode.childNode != null) createDecisionSet (currentNode.childNode);
	}
	
	public void getDecisionList () {
		Node temp;
		for (int i=0; i<decisionNodes.size(); i++) {
			temp = decisionNodes.get(i);
			String result = decisionNodes.get(i).publishResult();
			String string = "";
			while (!temp.equals(root)) {
				string = temp.nodeInformation() + ",\t" + string;
				temp = temp.parentNode;
			}
			System.out.println(i+1 + "." + string);
			System.out.println("Result: " + result);
		}
	}
	
	public String makeDecision(int [] condition) {
		Node decision = searchForDecisionNode(condition, root);
		//System.out.println(decision.publishResult());
		return decision.publishResult();
	}

	private Node getChild (Node parent, int childAddress) {
		Node temp = parent.childNode;
		for (int i=0; i<childAddress; i++) temp = temp.siblingNode;
		return temp;
	}
	
	private Node searchForDecisionNode (int[] condition, Node currentNode) {
		while (!currentNode.isLeaf) {
			int variableIndex = currentNode.variable.columnNumber;
			int index = condition[variableIndex];
			currentNode = getChild(currentNode, index);
		}
		return currentNode;
	}
	
	private void trainAndTestDataGenerate() {
		Set <Integer> testSet = new HashSet <Integer> ();
		int collectionSize = decisionSet.length;
		Random numberGenerator = new Random ();
		
		while (testSet.size() < (int)(collectionSize*0.2)) {
			int number = numberGenerator.nextInt(collectionSize);
			testSet.add(number);
		}
		
		trainData = new ArrayList <Integer> ();
		testData = new ArrayList <Integer> ();
		for (int i=0; i<collectionSize; i++) {
			if (testSet.contains(i)) {
				testData.add(i);
				continue;
			}
			trainData.add(i);
		}
		
		int [] decisionCount = new int [Attribute.numberOfDecisions];
		for (int i=0; i<testData.size(); i++) {
			int currentDecision = decisionSet[testData.get(i)];
			decisionCount[currentDecision]++;
		}
		
		defaultDecision = 0;
		for (int i=0; i<Attribute.numberOfDecisions; i++) {
			if (decisionCount[i] > decisionCount[defaultDecision]) {
				this.defaultDecision = i;
			}
		}
	}
	
	public void accuracyTesting() {
		int error = 0;
		int correct = 0;
		
		for (int i=0; i<testData.size(); i++) {
			int index = testData.get(i);
			Node decision = searchForDecisionNode(data[index], root);
			int result = decision.getResult();
			if (result == -1) result = defaultDecision;
			
			//System.out.println(Attribute.decisionAttributeName[result] + 
					//" -> " + Attribute.decisionAttributeName[decisionSet[index]]);
			if (result == decisionSet[index]) correct++;
			else error++;
		}
		
		this.accuracy = ((double)correct*100)/(correct+error);
	}

}