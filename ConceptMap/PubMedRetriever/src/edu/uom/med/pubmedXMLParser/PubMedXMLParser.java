package edu.uom.med.pubmedXMLParser;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Project takes one or more PMIDs and uses them to retrieve PubMed abstracts from the local version of PubMed database. 
 * The retrieved articles are processed to change the abbreviations to the original expanded name and to segment the 
 * PubMed abstracts into individual sentences. We use abbreviation expansion algorithm from our previous study for 
 * converting abbreviations to the expanded name and have incorporated the respective script into this project. Each 
 * sentence from an abstract is assigned with a PMID. This helps to map the sentence back to the abstract from which it is
 * being derived.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PubMedXMLParser {
	
	//global attribute
	static AbbreviationResolver abbreviationResolver = new AbbreviationResolver();
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String pmid="";
		int count1=0;
		
		String arg1 = args[0]; //input file
		String arg2 = args[1]; //output file
		
		ArrayList<Integer> pubMedIDList = new ArrayList<Integer>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
			
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		    while((pmid = br.readLine())!=null) {
				pubMedIDList.add(Integer.parseInt(pmid.trim()));
			}
			
			ArrayList<String> allSentences = new ArrayList<String>();
			for(int pubMedID : pubMedIDList) {
				ArrayList<String> sentences = search(NumericRangeQuery.newIntRange("pmid", pubMedID, pubMedID,true,true));
				if(!sentences.isEmpty()) {
					for(String each : sentences) {
						allSentences.add(pubMedID+"\t"+each);
					}
				}
					
				count1++;
				//if(count1==10) break;
				if(count1%10000==0) {
					long stopTime = System.currentTimeMillis();
				    long elapsedTime = stopTime - startTime;
				    System.out.println(count1+" abstracts were processed in "+elapsedTime);
				}
			}
			
			for(String each : allSentences) {
				bw.append(each);
				bw.append("\n");
			}
			
			System.out.println("Number of citations: "+pubMedIDList.size());
			System.out.println("Number of sentences: "+allSentences.size());
			
			br.close();
			bw.close();
		} catch (IOException ie) {
			System.err.println(ie);
		}
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime);
	}
	
	/**
	 * Method to search for a PubMed abstract using PMID. It uses Apache Lucene for the search.
	 * 
	 * @param q
	 * @return
	 * @throws IOException
	 */
	
	public static ArrayList<String> search(Query q) throws IOException {
		String filePath="", fileContents="", title="", citationAbstract=""; 
		int indexCount=1;
		
		ArrayList<String> sentences = new ArrayList<String>();
		
		while(indexCount<=27) {
		//while(indexCount<=13) {	
			filePath ="/net/psoriasis/home/rkalpana/Local_MEDLINE/indexed_files/"+indexCount;
			//filePath ="/net/psoriasis/home/rkalpana/Projects/Literature_based_disease_comorbidity/MEDLINE_2016version/Local_MEDLINE/"+indexCount;
    		Path path = Paths.get(filePath);
    		Directory directory = FSDirectory.open(path);
    		IndexReader reader = DirectoryReader.open(directory);
    		try {
    			IndexSearcher searcher = new IndexSearcher(reader);
    			ScoreDoc[] hits = searcher.search(q, 10).scoreDocs;
    			
    			if(hits.length>=1) {
    				int docId = hits[0].doc;
    				Document d = searcher.doc(docId); 
    				fileContents = d.get("contents");
    				String[] arrFileContents = fileContents.split("\n");
    				
    				title = arrFileContents[3].substring(6);
    				citationAbstract = arrFileContents[4].substring(9);
					
    				ArrayList<String> abstractSentences = abbreviationResolver.resolveAbbreviation(citationAbstract);
    				ArrayList<String> titleSentences = abbreviationResolver.segmentSentences(title);
    				sentences = new ArrayList<String>(titleSentences);
    				sentences.addAll(abstractSentences);
    				
    				break;
    			}
    		} finally {
    			if (reader != null)
    				reader.close();
    		}
    		indexCount++;
		}
		
		return sentences;
	}
	
}

