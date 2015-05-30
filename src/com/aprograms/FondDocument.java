package com.aprograms;



public class FondDocument {
	String docNumber;
	String docName;
	String docDates;
	String pagesNumber;
	String comments;
		
	int fondNumber;
	
	public String _toString(){
		return 
				"docNumber=" + docNumber 
				+ "\tdocName=" + docName 
				+ "\tdocDates=" + docDates 
				+ "\tpagesNumber=" + pagesNumber
				+ "\tcomments=" + comments;
	}
	
	public String toString(){
		return "Fond document"
				+ "\n\t" + docNumber 
				+ "\n\t" + docName 
				+ "\n\t" + docDates 
				+ "\n\t" + pagesNumber
				+ "\n\t" + comments;
	}
}
