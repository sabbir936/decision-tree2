package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Data {
	int[][] data;
	int[] decisionSet;
	int[] numberOfAttributes;

	String[][] attributeList;
	String[] decisionAttributeNames;
	String[] variableName;
	String decisionVariableName;
	String fileName;

	public Data(String file_name) {
		this.fileName = file_name;
		receiveDataFromFile();
		Variable.dataSet = data;
		Attribute.decisionSet = decisionSet;
		Attribute.decisionAttributeName = decisionAttributeNames;
		Variable.decisionSet = decisionSet;
	}

	public void receiveDataFromFile() {
		Scanner in = null;
		try {
			//in = new Scanner(new File("weather.arff"));
			//in = new Scanner(new File("lenses.arff"));
			//in = new Scanner(new File("car_evaluation.arff"));
			in = new Scanner(new File(fileName));
			//in = new Scanner(new File("balance.arff"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found");
			return;
		}

		String line, temp;
		String[] temporaryArray;
		ArrayList<String> attributeString = new ArrayList<String>();

		while (in.hasNextLine()) {
			line = in.nextLine();

			if (line.equals("") || line.charAt(0) == '%')
				continue;

			if (line.charAt(0) == '@' && line.startsWith("attribute", 1)) {
				temp = line.substring(10, line.length() - 1);
				attributeString.add(temp);
			}

			if (line.equals("@data"))
				break;
		}

		int numberOfIndependentVar = attributeString.size() - 1;

		attributeList = new String[numberOfIndependentVar][];
		variableName = new String[numberOfIndependentVar];
		numberOfAttributes = new int[numberOfIndependentVar];
		Map<String, Integer>[] strToInt = new Map[numberOfIndependentVar + 1];

		for (int i = 0; i < numberOfIndependentVar; i++) {
			temp = attributeString.get(i);
			temp = temp.substring(1, temp.length());
			temporaryArray = temp.split(" \\{");
			variableName[i] = temporaryArray[0];

			strToInt[i] = new HashMap<String, Integer>();
			temporaryArray[1] = temporaryArray[1].replaceAll(" ", "");
			attributeList[i] = temporaryArray[1].split(",");
			numberOfAttributes[i] = attributeList[i].length;

			for (int j = 0; j < attributeList[i].length; j++) {
				strToInt[i].put(attributeList[i][j], j);
			}
		}

		temp = attributeString.get(numberOfIndependentVar);
		temp = temp.substring(1, temp.length());
		temporaryArray = temp.split(" \\{");
		decisionVariableName = temporaryArray[0];

		temporaryArray[1] = temporaryArray[1].replaceAll(" ", "");
		decisionAttributeNames = temporaryArray[1].split(",");
		int numberOfDecisionAttributes = decisionAttributeNames.length;
		Attribute.numberOfDecisions = numberOfDecisionAttributes;
		Variable.numberOfDecisions = numberOfDecisionAttributes;

		strToInt[numberOfIndependentVar] = new HashMap<String, Integer>();
		for (int j = 0; j < numberOfDecisionAttributes; j++) {
			strToInt[numberOfIndependentVar].put(decisionAttributeNames[j], j);
		}

		ArrayList<Integer[]> dataList = new ArrayList<>();
		ArrayList<Integer> decisionData = new ArrayList<>();

		while (in.hasNextLine()) {
			line = in.nextLine();

			if (line.equals("") || line.charAt(0) == '%')
				continue;

			temp = line;
			temporaryArray = temp.split(",");
			String keyString;

			Integer[] temporaryIntArray = new Integer[numberOfIndependentVar];
			for (int j = 0; j < numberOfIndependentVar; j++) {
				keyString = temporaryArray[j];
				temporaryIntArray[j] = strToInt[j].get(keyString);
			}

			dataList.add(temporaryIntArray);
			keyString = temporaryArray[numberOfIndependentVar];
			decisionData.add(strToInt[numberOfIndependentVar].get(keyString));
		}

		decisionSet = new int[decisionData.size()];
		data = new int[decisionData.size()][numberOfIndependentVar];

		for (int i = 0; i < decisionData.size(); i++) {
			decisionSet[i] = decisionData.get(i);
			Integer[] temporaryIntArray = dataList.get(i);
			for (int j = 0; j < numberOfIndependentVar; j++) {
				data[i][j] = temporaryIntArray[j];
			}
		}

		in.close();
	}

}