package project;

import java.util.ArrayList;

public class Attribute {
	public static int numberOfDecisions;
	public static int [] decisionSet;
	public static String [] decisionAttributeName;
	public double entropy = 0;
	public double giniIndex = 1.0;
	public ArrayList <Integer> availableRows;
	public String attributeName;
	public int result;
	
	public Attribute (ArrayList <Integer> necessaryData, String name) {
		this.attributeName = name;
		this.availableRows = necessaryData;
		calculateEntropy();
		calculateGiniIdex();
	}
	
	private void calculateGiniIdex() {
		int [] numberOfCollection = new int [numberOfDecisions]; //i.e. how many win or lose contains
		int totalData = availableRows.size();
		double probablity;
		int index;
		
		for (int i=0; i<availableRows.size(); i++) {
			index = availableRows.get(i);
			numberOfCollection[decisionSet[index]]++;
		}
		
		result = 0;
		for (int i=0; i<numberOfDecisions; i++) {
			if (totalData == 0) {
				this.giniIndex = 1.00;
				this.result = -1;
				break;
			}
			probablity = (double)numberOfCollection[i]/totalData;
			if (probablity == 0.00) this.giniIndex += 0;
			else {
				this.giniIndex -= probablity * probablity;
			}
			
			if (numberOfCollection[i] > numberOfCollection[result]) this.result = i;
		}
	}
	
	private void calculateEntropy() {
		int [] numberOfCollection = new int [numberOfDecisions]; //i.e. how many win or lose contains
		int totalData = availableRows.size();
		double probablity;
		int index;
		
		for (int i=0; i<availableRows.size(); i++) {
			index = availableRows.get(i);
			numberOfCollection[decisionSet[index]]++;
		}
		
		result = 0;
		for (int i=0; i<numberOfDecisions; i++) {
			if (totalData == 0) {
				this.entropy = 0.00;
				this.result = -1;
				break;
			}
			probablity = (double)numberOfCollection[i]/totalData;
			if (probablity == 0.00) this.entropy += 0;
			else {
				this.entropy -= probablity * (Math.log(probablity) / Math.log(numberOfDecisions));
			}
			
			if (numberOfCollection[i] > numberOfCollection[result]) this.result = i;
		}
	}
	
	public String getProbability() {
		
		String resultWithProbability = "";
		int [] numberOfCollection = new int [numberOfDecisions];
		int totalData = availableRows.size();
		
		for (int i=0; i<availableRows.size(); i++) {
			int index = availableRows.get(i);
			numberOfCollection[decisionSet[index]]++;
		}
		
		for (int i=0; i<numberOfDecisions; i++) {
			double probablity = (double)numberOfCollection[i]/totalData;
			if (totalData == 0) probablity = 0.00;
			resultWithProbability += decisionAttributeName[i] + ": " + ((int)(probablity*10000))/(double)100 + "%   ";
		}
		
		return resultWithProbability;
	}
}
