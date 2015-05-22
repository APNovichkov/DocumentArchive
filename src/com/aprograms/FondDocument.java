package com.aprograms;

import org.w3c.dom.Node;


public class FondDocument {
	String docNumber;
	String docName;
	String docDates;
	String pagesNumber;
	String comments;
	
	int fondNumber;
	
	Node node;

	public FondDocument(int fondNumber, Node node){
		this.fondNumber = fondNumber;
		this.node = node;
				
	}
	
	
	
	
	
	
	public String toString(){
		return docNumber + docName + docDates + pagesNumber + comments;
	}
}
