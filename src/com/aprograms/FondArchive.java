package com.aprograms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class FondArchive {

	Vector<FondGroup> fondGroups = new Vector<FondGroup>();

	public void print() {
		for(FondGroup fg : fondGroups){
			fg.print();
		}		
	}
	
	public void dumpCSV() throws IOException{
		int fgId = 0;
		int fnId = 0;
		int fdId = 0;
				
		
		BufferedWriter fgWriter = new BufferedWriter(new FileWriter("FondGroup.csv"));
		BufferedWriter fnWriter = new BufferedWriter(new FileWriter("Fond.csv"));
		BufferedWriter fdWriter = new BufferedWriter(new FileWriter("FondDocumnet.csv"));
		
		for(FondGroup fg: fondGroups){
			fgWriter.write(
					"" + (++fgId) 
					+ "," + addQuotes(fg.name) 
					+ "\n");
			
			for(Fond fn: fg.fonds){
				fnWriter.write(
						"" + (++fnId) 
						+ "," + fgId
						+ "," + addQuotes(fn.fondName)
						+ "," + addQuotes(fn.fondSubName)
						+ "\n");
				for(FondDocument fd: fn.docs){
					fdWriter.write(
							"" + (++fdId)
							+ "," + fnId
							+ "," + addQuotes(fd.docNumber)
							+ "," + addQuotes(fd.docName)
							+ "," + addQuotes(fd.docDates)
							+ "," + addQuotes(fd.pagesNumber)
							+ "," + addQuotes(fd.comments)
							+ "," + addQuotes(fd.tags)
							+ "," + addQuotes(fd.nonTags)
							+ "\n"
							);
				}
			}
		}
		
		
		fgWriter.close();
		fnWriter.close();
		fdWriter.close();		
	}
	
	public String addQuotes(String val){
		return val == null? "\"\"" : "\"" + val + "\"";
				
	}
	
	
}
