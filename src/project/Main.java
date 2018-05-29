package project;

import java.io.File;

import javax.swing.JOptionPane;

public class Main {
	public static void main( String[] args ) {
		String fileName = JOptionPane.showInputDialog("Enter File Name/Path:");
		Properties.choice = JOptionPane.showInputDialog("press 1 for Entropy and any keys for gini impurity");
		File file = new File(fileName);
		while (!file.exists()) {
			JOptionPane.showMessageDialog(null, "File doesn't exist.");
			fileName = JOptionPane.showInputDialog("Enter file name:");
			file = new File(fileName);
		}
		Tree myTree = new Tree (new Data(fileName));
		new SelectionPage(myTree, fileName);
	}
	
	
}
