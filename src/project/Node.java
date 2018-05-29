package project;

public class Node {
	public Node parentNode = null;
	public Node childNode = null;
	public Node siblingNode = null;
	public Variable variable;
	public Attribute attribute;
	public boolean isLeaf = false;
	
	public Node (Attribute attribute) {
		this.attribute = attribute;
		this.isLeaf = true;
	}
	
	public Node (Variable identity, Attribute attribute) {
		this.variable = identity;
		this.attribute = attribute;		
	}
	
	public void setVariable (Variable variable) {
		this.variable = variable;
	}
	
	public void details () {
		for (int i=0; i<this.variable.attributes.length; i++) {
			this.variable.attributes[i].getProbability();
			System.out.println(this.variable.availableRows);
			System.out.println();
		}
	}
	public void addSibling (Node sibling) {
		sibling.parentNode = this.parentNode;
		if (this.siblingNode == null) this.siblingNode = sibling;
		else this.siblingNode.addSibling(sibling);
	}
	
	public void addChild (Node child) {
		child.parentNode = this;
		if (this.childNode == null) this.childNode = child;
		else this.childNode.addSibling(child);
	}
	
	public String nodeInformation () {
		return this.parentNode.variable.variableName + ": " + this.attribute.attributeName;
	}
	
	public String publishResult () {
		if (isLeaf) {
			if (this.attribute.availableRows.size() == 0) {
				if (this.parentNode.attribute != null) return this.parentNode.attribute.getProbability();
			}
			else return this.attribute.getProbability();
		}
		return "No Result";
	}
	
	public int getResult () {
		if (isLeaf) {
			if (this.attribute.availableRows.size() == 0) {
				if (this.parentNode.attribute != null) return this.parentNode.attribute.result;
			}
			else return this.attribute.result;
		}
		return -1;//this.attribute.result;
	}
	
	public String toString () {
		String detailsInfo;
		
		if (this.parentNode != null) detailsInfo = this.parentNode.variable.variableName + "(" + this.attribute.attributeName + "):";
		else detailsInfo = "Root";
		
		if (this.isLeaf) detailsInfo += " -> " + "Decision Variable";
		else detailsInfo += this.variable.variableName;
		
		return detailsInfo;
	}
}
