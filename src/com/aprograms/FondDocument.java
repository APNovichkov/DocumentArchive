package com.aprograms;



public class FondDocument {
	String docNumber;
	String docName;
	String docDates;
	String pagesNumber;
	String comments;
		
	String tags;
	String nonTags;
	
	
	final String TAG_NAME ="ярлыки:";
	
	public String toString(){
		return "Fond document"
				+ ":\t" + docNumber 
//				+ "\t" + docName 
//				+ "\t" + docDates 
//				+ "\t" + pagesNumber
//				+ "\t" + comments
				+ "\tnonTags = " + nonTags
				+ "\ttags = " + tags
				;
	}
	
	
	public void parseComments(){
		int pos = comments.indexOf(TAG_NAME);
		if(pos >= 0){
			nonTags = comments.substring(0, pos).trim();
			tags = comments.substring(pos + TAG_NAME.length()).trim();
		}
	}
}
