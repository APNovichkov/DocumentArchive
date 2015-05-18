package com.aprograms;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocumentParser {

	static File file; 
	
	public void run() throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		parseDocument(doc);
		
//		test1(doc, "");
		
	}
	
	
	
	private void parseDocument(Node node) {
		if(node.getNodeName().equals("w:tbl")){
			parseTable(node);
		} else{
			NodeList nodes = node.getChildNodes();
			for(int i = 0; i < nodes.getLength(); i++){			
				parseDocument(nodes.item(i));			
			}		
		}
	}
	
	



	private void parseTable(Node node) {
		System.out.println("Table found");
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
