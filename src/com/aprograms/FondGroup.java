package com.aprograms;

import java.util.Vector;

public class FondGroup {
	String name;
	Vector<Fond> fonds = new Vector<Fond>();
	
	public void print() {
		System.out.println("Fond group: " + name);
		for(Fond fond: fonds){
			fond.print();
		}
		
		
	}
		
	
}
