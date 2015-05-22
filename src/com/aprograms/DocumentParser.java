package com.aprograms;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocumentParser {

	static File file; 
	
	public int fondDocumentNumber = 1;
	
	Vector<FondDocument> documents;
	
	public void run() throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		parseDocument(doc);
	}
	
	
	
	private void parseDocument(Node node) {
		if(node.getNodeName().equals("w:tbl")){
			System.out.println("Table found");
			parseTable(node);
		}else{
			NodeList nodes = node.getChildNodes();
			for(int i = 0; i < nodes.getLength(); i++){			
				parseDocument(nodes.item(i));
				
			}		
		}
	}
	
	private void parseTable(Node node) {
		if(node.getNodeName().equals("w:tr")){
			System.out.println("\t" + "Row Found");
			documents = new Vector<FondDocument>(fondDocumentNumber);
		}else{
			NodeList nodes = node.getChildNodes();
			for(int i = 0; i < nodes.getLength(); i++){			
				parseTable(nodes.item(i));
			}		
		}
	}

/*

	private void parseRow(Node node) {
		if(node.getNodeName().equals("w:tc")){
			System.out.println("\t" + "\t" + "got cell");
			tabController = 1;
			getCell(node);
			System.out.println();
		}else{
			NodeList nodes = node.getChildNodes();
			for(int i = 0; i < nodes.getLength(); i++){			
				parseRow(nodes.item(i));
				
			}		
		}
	}

	private void getCell(Node node) {
		if(node.getNodeName().equals("w:t")){
			if(tabController == 1){
				System.out.print("\t" + "\t" + "\t");
				tabController++;
			}
			System.out.print(node.getTextContent());
		}else{
			NodeList nodes = node.getChildNodes();
			for(int i = 0; i < nodes.getLength(); i++){			
				getCell(nodes.item(i));
				
			}		
		}
		
		
		
	}

*/

//	private void getText(Node node) {
		
//	}

	
	private void getText(Node node){
		StringBuffer sb = new StringBuffer();
		getText(node, sb);
		String str = sb.toString();
	}
	
	private void getText(Node node, StringBuffer sb){
		sb.append("sfwer");
		
		getText(node, sb);
	}
	
	


	private void test1(Node node, String prefix) {
		
		if(node.getNodeName().equals("w:tbl")){
			System.out.println(node.getTextContent());
		}
			
		NodeList nodes = node.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++){			
			test1(nodes.item(i), prefix + "\t");			
		}
		
	}



	public static void main(String[] args) throws Exception {					
		file = new File(args[0]);
		new DocumentParser().run();		
	}

}
