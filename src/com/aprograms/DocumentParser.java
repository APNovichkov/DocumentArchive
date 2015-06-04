package com.aprograms;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocumentParser {

	static File file; 
	

	FondArchive fondArchive = new FondArchive();
	FondGroup currentFondGroup = null;
	Fond currentFond = null;
	
	public final int NONE = 1;
	public final int FONDGROUP_HEADER_FOUND = 2;
	public final int FONDHEADER_FOUND = 3;
	public final int TABLE_HAS_BEEN_PARSED = 4;
	
	public int currentParsingStep = NONE;
	
	public void run() throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		parseDocument(doc);
		fondArchive.print();
	}
	
	


	/**
	 * Parsing the document
	 * @param node
	 */
	private void parseDocument(Node node) {
		if(node.getNodeName().equals("w:p")){
			if(isNodeAFondOrFondGroup(node, "Heading1")){
				switch(currentParsingStep){
					case NONE:
					case TABLE_HAS_BEEN_PARSED: {
						createFondGroup(node);
						System.out.println("New Fond Group Created");
						currentParsingStep = FONDGROUP_HEADER_FOUND;					
						break;
					}
					case FONDGROUP_HEADER_FOUND:{
						System.out.println("ERROR: new fondgroup found after groupFond");
						currentParsingStep = NONE;
						break;
					}
					case FONDHEADER_FOUND:{
						System.out.println("ERROR: new fondgroup found after fond");
						currentParsingStep = NONE;
						break;
					}
				}
			}
			if(isNodeAFondOrFondGroup(node, "Heading3")){
				switch(currentParsingStep){
				
					case FONDGROUP_HEADER_FOUND:
					case FONDHEADER_FOUND:
					case TABLE_HAS_BEEN_PARSED:{
						createFond(node);
						System.out.println("New Fond Created");
						currentParsingStep = FONDHEADER_FOUND;					
						break;
					}
/*					
					case FONDHEADER_FOUND:{
						System.out.println("ERROR: new fond found after fond");
						currentParsingStep = NONE;
						break;
					}
*/					
					case NONE:{
						System.out.println("ERROR: new fond found after something none");
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
				case FONDGROUP_HEADER_FOUND:{
					System.out.println("ERROR: table found but no fondGroup header was found");
					currentParsingStep = NONE;
					break;
				}
				case FONDHEADER_FOUND:{
					parseTable(node);
					System.out.println("Table has been parsed");
					currentParsingStep = TABLE_HAS_BEEN_PARSED;
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
	

	//PARSING THE TABLE
	
	/**
	 * 
	 * @param node
	 */
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
		doc.parseComments();
		
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
	
	
	//GETTING FOND, GROUPFOND TEXT, OR FOND SUBNAME TEXT
	
	private String getFondOrFondGroupText(Node node){
		StringBuffer sb = new StringBuffer();
		collectText(node, sb);
		return sb.toString();
	}
	
	/**
	 * 
	 * @param node
	 * @param sb
	 */
	private void collectText(Node node, StringBuffer sb ){
		
		NodeList nodes = node.getChildNodes();
		if(node.getNodeName().equals("w:r")){
			sb.append(node.getTextContent());
		}
		for(int i = 0; i < nodes.getLength(); i++){			
			collectText(nodes.item(i), sb);
		}		
	}
	

	private String getFondSubName(Node node){
		NodeList nodes = node.getChildNodes();
		String fondSubName = null;
		if(node.getNodeName().equals("w:r")){
			fondSubName = node.getTextContent();
		}
		for(int i = 0; i < nodes.getLength(); i++){			
			getFondSubName(nodes.item(i));
		}		
		return fondSubName;
	}

	
	//IS HEADING A FOND, FONDGROUP, OR SUBNAME OF A FOND
	
	
	private boolean isNodeAFondOrFondGroup(Node node, String heading) {
		
		Node pprNode = node.getFirstChild(); 
		if(pprNode == null) return false;
		if(!pprNode.getNodeName().equals("w:pPr")) return false;
		
		Node styleNode = pprNode.getFirstChild();
		if(styleNode == null) return false;
		if(!styleNode.getNodeName().equals("w:pStyle")) return false;
		
		NamedNodeMap attrs = styleNode.getAttributes();
		return attrs.getNamedItem("w:val").getNodeValue().equals(heading);

	}

	
	//CREATING THE FOND, FOND GROUP, AND FOND ARCHIVE 
	
	
	private void createFondGroup(Node node) {
		currentFondGroup = new FondGroup();
		
		currentFondGroup.name = getFondOrFondGroupText(node);
		fondArchive.fondGroups.add(currentFondGroup);
	}

	private void createFond(Node node) {
		currentFond = new Fond();
			
		currentFond.fondName = getFondOrFondGroupText(node);
		currentFond.fondSubName = getFondSubName(node.getNextSibling());
		currentFondGroup.fonds.add(currentFond);
	}
	
	public static void main(String[] args) throws Exception {					
		file = new File(args[0]);
		new DocumentParser().run();		
	}

}
