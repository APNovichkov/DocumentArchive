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
	
//	public int fondDocumentNumber = 1;
	
	Vector<FondDocument> documents = new Vector<FondDocument>();
	
	Vector<Fond> fonds = new Vector<Fond>();
	
	public void run() throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
//		parseDocument(doc);
//		printFondDocuments();
		_printHeading3(doc);
	}
	
	
	
	private void _printHeading3(Node node) {
		NodeList nodes = node.getChildNodes();
		if(node.getNodeName().equals("w:pPr")){
			isFond(node);
		}
		for(int i = 0; i < nodes.getLength(); i++){			
			_printHeading3(nodes.item(i));
		}		
	}

	private void isFond(Node node) {
		NodeList nodes = node.getChildNodes();
		NamedNodeMap attrs = node.getAttributes();
		
		if(node.getNodeName().equals("w:pStyle") && attrs.getNamedItem("w:val").getNodeValue().equals("Heading3")){
			System.out.println("attribute found");
//			getFondText(node);
		}
	}



	private void getFondText(Node node){
		NodeList nodes = node.getChildNodes();

	}

	private void _printAttributes(Node node, NamedNodeMap attrs) {
		
		if(attrs == null){
			System.out.println("No atttributes");
		} else{
			for(int i = 0; i < attrs.getLength(); i++){
				System.out.println(attrs.item(i));
			}
		}
	}



	private void printFondDocuments() {
		for(FondDocument fd: documents){
			System.out.println(fd.toString());			
		}		
	}



	private void parseDocument(Node node) {
		
		if(node.getNodeName().equals("w:tbl")){
//			System.out.println("Table found");
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
//			System.out.println("\t" + "Row Found");
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
	
	

	
public static void main(String[] args) throws Exception {					
		file = new File(args[0]);
		new DocumentParser().run();		
	}

}
