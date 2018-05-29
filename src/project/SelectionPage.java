package project;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import java.awt.Font;

public class SelectionPage {

	private JFrame frame;
	private int numberOfVariables;
	private int [] testCase;
	private JComboBox [] comboBox;
	private JTextArea textArea;
	private Tree decisionTree;
	private String fileName;

	public SelectionPage(Tree decisionTree, String fileName) {
		this.decisionTree = decisionTree;
		this.fileName = fileName;
		this.numberOfVariables = decisionTree.variable.length;
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Decision Predictor");
		frame.setBounds(100, 100, 800, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel firstPanel = new JPanel();
		firstPanel.setBounds(20, 100, 725, 30);
		frame.getContentPane().add(firstPanel);
		firstPanel.setLayout(new BoxLayout(firstPanel, BoxLayout.X_AXIS));
		
		JPanel secondPanel = new JPanel();
		secondPanel.setBounds(20, 150, 725, 30);
		frame.getContentPane().add(secondPanel);
		secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.X_AXIS));
		
		JButton submitButton = new JButton("Submit");
		
		submitButton.setBounds(650, 200, 95, 25);
		frame.getContentPane().add(submitButton);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(20, 200, 620, 25);
		textArea.setFont(new Font("Arial Black", Font.PLAIN, 12));
		textArea.setForeground(Color.DARK_GRAY);
		frame.getContentPane().add(textArea);
		
		JLabel authorLabel = new JLabel("Decision tree");
		authorLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		authorLabel.setEnabled(false);
		authorLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		authorLabel.setBounds(710, 240, 70, 20);
		frame.getContentPane().add(authorLabel);
		
		JButton rulesButton = new JButton("Print Rules");
		rulesButton.setBounds(650, 30, 95, 25);
		frame.getContentPane().add(rulesButton);
		
		String temporary = String.format("%.2f", decisionTree.accuracy) + " %";
		JLabel accuracyLabel = new JLabel(temporary);
		accuracyLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
		accuracyLabel.setBounds(539, 30, 100, 25);
		frame.getContentPane().add(accuracyLabel);
		
		JLabel tagLabel = new JLabel("Accuracy: ");
		tagLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		tagLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
		tagLabel.setBounds(435, 30, 100, 25);
		frame.getContentPane().add(tagLabel);
		
		JLabel fileLabel = new JLabel("File Name: " + fileName);
		fileLabel.setVerticalAlignment(SwingConstants.TOP);
		fileLabel.setFont(new Font("Arial Black", Font.PLAIN, 15));
		fileLabel.setBounds(20, 30, 400, 25);
		frame.getContentPane().add(fileLabel);
		
		comboBox = new JComboBox [numberOfVariables];
		JLabel [] labeling = new JLabel [numberOfVariables];
		for (int i=0; i<numberOfVariables; i++) {
			String [] temp = decisionTree.attributeList[i];
			comboBox[i] = new JComboBox <String> ();
			labeling[i] = new JLabel ("  " + decisionTree.variable[i] + ": ");
			comboBox[i].setModel(new DefaultComboBoxModel <String> (temp));
			comboBox[i].setSize(70, 30);
			labeling[i].setSize(80, 25);
			comboBox[i].setBounds(20, 100, 75, 30);
		}
		
		
		for (int i=0; i<numberOfVariables; i++) {
			if (i%2 == 0) {
				firstPanel.add(labeling[i]);
				firstPanel.add(comboBox[i]);
			}
			
			else {
				secondPanel.add(labeling[i]);
				secondPanel.add(comboBox[i]);
			}
		}
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getTestVariables();
				String temp = decisionTree.makeDecision(testCase);;
				textArea.setText(temp);
				textArea.setSelectedTextColor(Color.BLUE);
				textArea.setAutoscrolls(true);
			}
		});
		
		rulesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				decisionTree.getDecisionList();
			}
		});
		
		frame.setVisible(true);
	}

	protected void getTestVariables() {
		testCase = new int [numberOfVariables];
		for (int i=0; i<numberOfVariables; i++) {
			testCase[i] = comboBox[i].getSelectedIndex();
		}
	}
}
