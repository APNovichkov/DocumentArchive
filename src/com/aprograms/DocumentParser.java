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
	
	Vector<Fond> fonds = new Vector<Fond>();
	Fond currentFond = null;
	
	public final int NONE = 1;
	public final int HEADERFOUND = 2;
	public final int PROCESSINGTABLE = 3;

	public int currentParsingStep = NONE;
	
	public void run() throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		parseDocument(doc);
		printFond();
	}
	
	
	//PARSING THE DOCUMENT
	

	private void parseDocument(Node node) {
		if(node.getNodeName().equals("w:p")){
			if(isNodeAFond(node)){
				
				switch(currentParsingStep){
					case NONE:{
						createFond(node);
						currentParsingStep = HEADERFOUND;					
						break;
					}
					case HEADERFOUND:{
						System.out.println("ERROR: new fond found after fond:" + currentFond.name);
						currentParsingStep = NONE;
						break;
					}
				}
			}
			
		}else if(node.getNodeName().equals("w:tbl") ){
			
			switch(currentParsingStep){
				case NONE:{
					System.out.println("ERROR: table found but no fond header was found");
					currentParsingStep = NONE;
					break;
				}
				case HEADERFOUND:{
					parseTable(node);
					currentParsingStep = NONE;
					break;
				}
			}
			
			
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
		
		currentFond.docs.add(doc);
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
	
	
	private boolean isNodeAFond(Node node) {
		
		Node pprNode = node.getFirstChild(); 
		if(pprNode == null) return false;
		if(!pprNode.getNodeName().equals("w:pPr")) return false;
		
		Node styleNode = pprNode.getFirstChild();
		if(styleNode == null) return false;
		if(!styleNode.getNodeName().equals("w:pStyle")) return false;
		
		NamedNodeMap attrs = styleNode.getAttributes();
		return attrs.getNamedItem("w:val").getNodeValue().equals("Heading3"); 
	}


	private String getFondText(Node node){
		StringBuffer sb = new StringBuffer();
		collectText(node, sb);
		return sb.toString();
	}
	
	private void collectText(Node node, StringBuffer sb ){
		
		NodeList nodes = node.getChildNodes();
		if(node.getNodeName().equals("w:r")){
			sb.append(node.getTextContent());
		}
		for(int i = 0; i < nodes.getLength(); i++){			
			collectText(nodes.item(i), sb);
		}		
	}
	
	
	
	
	//CREATING THE FOND AND PUTTING IT INTO THE VECTOR
	

	private void createFond(Node node) {
		
		currentFond = new Fond();
			
		currentFond.name = getFondText(node);
		fonds.add(currentFond);		
//		System.out.println("fond created");
	}
	
	
	//PRINTING THE FONDS
	
	private void printFond(){
		for(int i = 0; i <= fonds.size(); i++){
			System.out.println(fonds.get(i).name + "\t\t\t" + fonds.get(i).docs);
		}
	}
	
	public static void main(String[] args) throws Exception {					
		file = new File(args[0]);
		new DocumentParser().run();		
	}

}
