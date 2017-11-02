package edu.uom.med.localPubmedDatabaseCompiler;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * The XMLProcessorAndIndexer program implements an application that 
 * parses PubMed files and compresses them into a binary format.
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */

public class XMLProcessorAndIndexer {
	
	String articleTitle="", pmid="", filteredPMID="", journaltitle="", preLine="", prePubLine="", title=""; 
	String year;
		
	ArrayList<String> meshTerms = new ArrayList<String>();
	ArrayList<String> abstractText = new ArrayList<String>();
	ArrayList<String> chemicalsList = new ArrayList<String>();
	ArrayList<String> publicationType = new ArrayList<String>();
		
	/** 
	 * Method retrieves information from PubMed articles within each XML file and creates a local 
	 * PubMed database in binary format. 
	 * 
	 * @param decomposedFile
	 * @return
	 */
	
	public void processorAndIndexer(String inputFolderName, String outputFolderName) {
		
		System.out.println("Compiling the local PubMed database....");
		
		String line, pmid="";
		String match, absText, abstractLine="", mesh="", chemical, pubYear;
		int no_of_pubmeds=0;
		
		try {
			File folder = new File(inputFolderName);
			File[] listOfFiles = folder.listFiles();
			
			Path path = Paths.get(outputFolderName);
			Directory directory = FSDirectory.open(path);
        	IndexWriterConfig config = new IndexWriterConfig(new SimpleAnalyzer());        
        	IndexWriter indexWriter = new IndexWriter(directory, config);
        	indexWriter.deleteAll();
        	
        	//iterate through all decompossed files
        	for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	String decompossedFileName = file.getName();
			    
			    	FileInputStream fis = new FileInputStream(inputFolderName+decompossedFileName);
			    	InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			    	BufferedReader br = new BufferedReader(isr);
			        
			    	while ((line = br.readLine()) != null) {
			    		//PMID
			    		if(line.trim().startsWith("<PMID ") && pmid.isEmpty()) {
			    			Pattern p = Pattern.compile(">(.+?)</PMID>");
			    			Matcher m = p.matcher(line);
			    			if(m.find()) {
			    				match = m.group().toString();
			    				pmid = match.substring(1, match.indexOf("</"));
			    			}
			    		}
					
			    		//year of publication 
			    		if(line.trim().startsWith("<PubDate>")) {
			    			preLine=line.trim();
			    		}
			    		if(line.trim().startsWith("<Year>") && preLine.equals("<PubDate>")) {
			    			Pattern p = Pattern.compile(">(.+?)</Year>");
			    			Matcher m = p.matcher(line);
			    			if(m.find()) {
			    				match = m.group().toString();
			    				pubYear = match.substring(1, match.indexOf("</"));
			    				year = pubYear; 
			    				preLine="";
			    			}
			    		}
			    		else if(line.trim().startsWith("<MedlineDate>") && preLine.equals("<PubDate>")) {
			    			Pattern p = Pattern.compile(">(.+?)</MedlineDate>");
			    			Matcher m = p.matcher(line);
			    			if(m.find()) {
			    				match = m.group().toString();
			    				pubYear = match.substring(1, match.indexOf("</"));
			    				year = pubYear;
			    				preLine="";
			    			}
			    		}
					
			    		//journal title
			    		if(line.trim().startsWith("<ISOAbbreviation>")) {
			    			journaltitle = line.substring(line.indexOf(">")+1,line.indexOf("</"));
			    			if(journaltitle.contains(".")) {
			    				journaltitle = journaltitle.replace(".", "");
			    			}
			    		}
					
			    		//article title
			    		if(line.trim().startsWith("<ArticleTitle>")){
			    			Pattern p = Pattern.compile(">(.+?)</ArticleTitle>");
			    			Matcher m = p.matcher(line.trim());
			    			if(m.find()) { //Q1
			    				match = m.group().toString();
			    				articleTitle = match.substring(1, match.indexOf("</"));
			    			}
			    		}
				
			    		//abstract
			    		if(line.trim().startsWith("<AbstractText>")) {
			    			Pattern p = Pattern.compile(">(.+?)</AbstractText>");
			    			Matcher m = p.matcher(line);
			    			if(m.find()) {
			    				match = m.group().toString();
			    				absText = match.substring(1, match.indexOf("</"));
			    				abstractText.add(absText);
			    			}
			    		}
			    		if(line.trim().startsWith("<AbstractText ")) {
			    			Pattern p = Pattern.compile(">(.+?)</AbstractText>");
			    			Matcher m = p.matcher(line);
			    			if(m.find()) {
			    				match = m.group().toString();
			    				absText = match.substring(1, match.indexOf("</"));
			    				String label=line.substring(0, line.indexOf(">")+1);
			    				abstractText.add(label.trim()+absText.trim());
			    			}
			    		}
			    		if(line.trim().startsWith("</Abstract>")) {
			    			for(String ab : abstractText) {
			    				if(abstractLine.equals("")) { abstractLine = ab; }
			    				else { abstractLine = abstractLine+" "+ab; }
			    			}
			    		}
				
			    		//MeSH terms
			    		String qName="";
			    		if(line.trim().startsWith("<DescriptorName")){
			    			Pattern p = Pattern.compile(">(.+?)</DescriptorName>");
			    			Matcher m = p.matcher(line);
			    			while(m.find()) {
			    				match = m.group().toString();
			    				mesh = match.substring(1, match.indexOf("</"));
			    			}
			    		}
			    		if(line.trim().startsWith("<QualifierName MajorTopicYN=\"Y\"")){
			    			Pattern p = Pattern.compile(">(.+?)</QualifierName>");
			    			Matcher m = p.matcher(line);
			    			while(m.find()) {
			    				match = m.group().toString();
			    				qName = match.substring(1, match.indexOf("</"));
			    				mesh = mesh.concat("<<").concat(qName);
			    			}
			    		}
			    		if(line.trim().startsWith("</MeshHeading>")){
			    			meshTerms.add(mesh);
			    			mesh="";
			    		}
				
			    		//chemicals list
			    		if(line.trim().startsWith("<NameOfSubstance")){
			    			Pattern p = Pattern.compile(">(.+?)</NameOfSubstance>");
			    			Matcher m = p.matcher(line);
			    			while(m.find()) {
			    				match = m.group().toString();
			    				chemical = match.substring(1, match.indexOf("</"));
			    				chemicalsList.add(chemical);
			    			}
			    		}
				
			    		//publication type
			    		if(line.trim().startsWith("<PublicationType ")) {
			    			String evidence = line.substring(line.indexOf(">")+1, line.indexOf("</PublicationType>"));
			    			publicationType.add(evidence);
			    		}
				
			    		//index MEDLINE abstract with Lucene
			    		if(line.trim().startsWith("</MedlineCitation>") && !pmid.isEmpty()) {
			    			StringBuffer stringBuffer = new StringBuffer();
			    			String meshList="", chemList="", pubList="";
						
			    			//grouping MeSH index, chemicals list and publication type
			    			if(!meshTerms.isEmpty()) {
			    				for(String meshTerm : meshTerms) {
			    					if(meshList.isEmpty()) { meshList=meshTerm; }
			    					else { meshList = meshList.concat("#").concat(meshTerm); }
			    				}
			    			}
			    			if(!chemicalsList.isEmpty()) {
			    				for(String chem : chemicalsList) {
			    					if(chemList.isEmpty()) { chemList=chem; }
			    					else { chemList = chemList.concat("#").concat(chem); }
			    				}
			    			}
			    			if(!publicationType.isEmpty()) {
			    				for(String ptype : publicationType) {
			    					if(pubList.isEmpty()) { pubList=ptype; }
			    					else { pubList = pubList.concat("#").concat(ptype); }
			    				}
			    			}
						
			    			//add MEDLINE abstract to stringBuffer
			    			stringBuffer.append("PMID-"+pmid).append("\n");
			    			stringBuffer.append("Year-"+year).append("\n");
			    			stringBuffer.append("Journal-"+journaltitle).append("\n");
			    			stringBuffer.append("Title-"+articleTitle).append("\n");
			    			stringBuffer.append("Abstract-"+abstractLine).append("\n");
			    			stringBuffer.append("MeSH-"+meshList).append("\n");
			    			stringBuffer.append("Chemicals-"+chemList).append("\n");
			    			stringBuffer.append("PublicationType-"+pubList).append("\n");
						
			    			int pmidInt = Integer.parseInt(pmid);
						
			    			//collect the binary format to an ArrayList
			    			String citation = stringBuffer.toString();
			    			String fileName = Integer.toString(pmidInt);
						
			    			Document doc = new Document();
			    			doc.add(new TextField("path", fileName, Store.YES));
			    			doc.add(new TextField("contents", citation, Store.YES));
			    			doc.add(new IntField("pmid", pmidInt, Field.Store.YES));
			    			indexWriter.addDocument(doc);
			   		 	
			    			pmid="";
			    			journaltitle="";
			    			articleTitle="";
			    			abstractLine="";
			    			meshTerms.clear();
			    			abstractText.clear();
			    			chemicalsList.clear();
			    			publicationType.clear();
						
			    			preLine="";
						
			    			no_of_pubmeds++;
			    			//if(no_of_pubmeds==1000) break;
			    			if(no_of_pubmeds%5000 == 0) System.out.println(no_of_pubmeds);
			    		}
			    	}
			    	
					br.close();
			    }
        	}
        	
			indexWriter.close();           
   		 	directory.close();
   		 	
   		 	System.out.println("Total number of pubmed articles processed: "+no_of_pubmeds++);
            
		} catch (IOException e) {
            System.out.println(e);
        }
	}
	
}
