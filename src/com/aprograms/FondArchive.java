package com.aprograms;

import java.util.Vector;

public class FondArchive {

	Vector<FondGroup> fondGroups = new Vector<FondGroup>();

	public void print() {
		for(FondGroup fg : fondGroups){
			fg.print();
		}
		
	}
}
