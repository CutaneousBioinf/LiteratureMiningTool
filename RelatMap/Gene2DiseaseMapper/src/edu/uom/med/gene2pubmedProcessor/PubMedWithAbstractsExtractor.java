package edu.uom.med.gene2pubmedProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
 * Program to retrieve PubMed ID with abstracts. The resource file gene2pubmed is previously processed to filter the 
 * records related to human.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PubMedWithAbstractsExtractor {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="";
		int count=0;
		
		String arg1 = args[0]; // input file 
		String arg2 = args[1]; // output file 
		
		ArrayList<Integer> pubmedIDList = new ArrayList<Integer>();
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine())!=null) {
				String[] arrLine = line.split("\t");
				pubmedIDList.add(Integer.parseInt(arrLine[0].trim()));
			}
			
			for(int pubMedID : pubmedIDList) {
				boolean flag = search(NumericRangeQuery.newIntRange("pmid", pubMedID, pubMedID,true,true));
				if(flag) {
					bw.append(Integer.toString(pubMedID));
					bw.append("\n");
					count++;
				}
				
				if(count%1000==0) System.out.println(count);
			}
			
			System.out.println("Number of PubMed articles with abstracts: "+count);
			br.close();
			bw.close();
		} catch (IOException ie) {
			System.err.println(ie);
		}
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time:"+elapsedTime);
	}
	
	/**
	 * Program to retrieve a PubMed abstract based on PubMed ID, from the local version of PubMed database
	 * 
	 * @param q
	 * @return
	 * @throws IOException
	 */
	
	public static boolean search(Query q) throws IOException {
		String filePath="", fileContents=""; 
		boolean flag = false;
		int indexCount=1;
		
		while(indexCount<=27) { //Number of folders with Local version of PubMed database
			filePath ="/Use/your/folder/name/Local_MEDLINE/indexed_files/"+indexCount;
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
    				
    				if(!arrFileContents[4].equals("Abstract-")) flag = true;
    					
    				break;
    			}
    		} finally {
    			if (reader != null)
    				reader.close();
    		}
    		indexCount++;
		}
		
		return flag;
	}
	
}

