package com.aprograms;

import java.util.Vector;

public class Fond {
	String fondName;
	String fondSubName;
	Vector<FondDocument> docs = new Vector<FondDocument>();
	
	public void print() {
		System.out.println("Fond: " + fondName);
		for(FondDocument doc: docs){
			System.out.println("\t" + doc);
		}
	}
}
