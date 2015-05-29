package com.aprograms;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocumentParser {

	static File file; 
	
	Vector<FondDocument> documents = new Vector<FondDocument>();
	Vector<Fond> fonds = new Vector<Fond>();
	Vector<String> headings = new Vector<String>();
	
	public void run() throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		parseDocument(doc);
		getHeading3(doc);
		createFond();
	}
	
	
	//PARSING THE DOCUMENT
	

	private void parseDocument(Node node) {
		
		if(node.getNodeName().equals("w:tbl")){
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
			parseRow(node);
		}else{
			NodeList nodes = node.getChildNodes();
			for(int i = 0; i < nodes.getLength(); i++){			
				parseTable(nodes.item(i));
			}		
		}
	}

	private void parseRow(Node node) {
	
		FondDocument doc = new FondDocument();				
		NodeList cells = node.getChildNodes();
		
		doc.docNumber = getText(cells.item(0));
		doc.docName = getText(cells.item(1));
		doc.docDates = getText(cells.item(2));
		doc.pagesNumber = getText(cells.item(3));
		doc.comments = getText(cells.item(4));
		
		documents.add(doc);
	}
	
	private String getText(Node node){
		StringBuffer sb = new StringBuffer();
		getText(node, sb);
		return sb.toString();
	}
	
	private void getText(Node node, StringBuffer sb){
		if(node.getNodeName().equals("w:t")){
			sb.append(node.getTextContent());
		}else{
			NodeList nodes = node.getChildNodes();
			for(int i = 0; i < nodes.getLength(); i++){			
				getText(nodes.item(i), sb);
				
			}		
		}
	}
	
	
	//GETTING THE HEADING
	
	
	private void getHeading3(Node node) {
		NodeList nodes = node.getChildNodes();
		if(node.getNodeName().equals("w:pPr")){
			System.out.println("Node Found");
			isNodeAFond(node);
		}
		for(int i = 0; i < nodes.getLength(); i++){			
			getHeading3(nodes.item(i));
		}		
	}

	private void isNodeAFond(Node node) {
		NodeList nodes = node.getChildNodes();
		NamedNodeMap attrs = node.getAttributes();
		
		if(node.getNodeName().equals("w:pStyle") && attrs.getNamedItem("w:val").getNodeValue().equals("Heading3")){
			getFondText(node.getParentNode().getParentNode());
		}
		for(int i = 0; i < nodes.getLength(); i++){			
			isNodeAFond(nodes.item(i));
		}		
	}


	private void getFondText(Node node){
		NodeList nodes = node.getChildNodes();
		
		if(node.getNodeName().equals("w:r")){
			headings.add(node.getTextContent());
		}
		for(int i = 0; i < nodes.getLength(); i++){			
			getFondText(nodes.item(i));
		}		
	}
	
	
	//CREATING THE FONDS AND PUTTING THEM INTO THE VECTOR
	

	private void createFond() {
		for(int i = 0; i <= headings.size(); i++){
			Fond fond = new Fond();
			
			fond.heading = headings.get(i);
			fond.doc = documents;
			
			fonds.add(fond);
		}
	}
	
	
	//PRINTING THE FONDS
	
	
	
	public static void main(String[] args) throws Exception {					
		file = new File(args[0]);
		new DocumentParser().run();		
	}

}
