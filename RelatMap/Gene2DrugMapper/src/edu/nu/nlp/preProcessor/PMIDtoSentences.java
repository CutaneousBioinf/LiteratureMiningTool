package edu.nu.nlp.preProcessor;


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

import edu.nu.nlp.preProcessor.AbbreviationResolver;

/**
 * Project retrieves PubMed abstracts from the local version of PubMed database for the give PMID. We use
 * Apache Lucene for this retrieval and it is used as an external library to the project.   
 * The retrieved abstracts are preprocessed in two stages: (1) Abbreviation expansion and (2) sentence
 * segmentation.
 * 
 * @author Kalpana Raja
 *
 */

public class PMIDtoSentences {
	
	//global attributes
	static AbbreviationResolver abbreviationResolver = new AbbreviationResolver();
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		String pmid=""; 
		int pubMedID=0, count=0, count1=0;
		
		try {
			FileReader fr = new FileReader("/net/psoriasis/home/rkalpana/Java_Applications/Input/gene2pubmed_Human_PMIDs.txt");
			BufferedReader br = new BufferedReader(fr);
			
			FileWriter fw = new FileWriter("/net/psoriasis/home/rkalpana/Java_Applications/Output/gene2pubmed_Human_pmidsentences.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			while((pmid = br.readLine()) != null) {
				pubMedID = Integer.parseInt(pmid.trim());
				ArrayList<String> sentences = search(NumericRangeQuery.newIntRange("pmid", pubMedID, pubMedID,true,true));
				if(!sentences.isEmpty()) {
					for(String each : sentences) {
						count++;
						bw.append(pmid+"\t"+each);
						bw.append("\n");
					}
				}
				count1++;
				
				//if(count1==100) break;
				//if(count1%10000==0) System.out.println(count1);
			}
			
			System.out.println("Total number of sentences: "+count);
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
	/**
	 * Method retrieves PubMed article for a give PMID from the local version of PubMed database
	 * 
	 * @param q
	 * @return
	 * @throws IOException
	 */
	
	public static ArrayList<String> search(Query q) throws IOException {
		String filePath="", fileContents="", pubmedID="", title="", citationAbstract=""; 
		int indexCount=1;
		
		ArrayList<String> sentences = new ArrayList<String>();
		
		while(indexCount<=27) {
			filePath ="/net/psoriasis/home/rkalpana/Local_MEDLINE/indexed_files/"+indexCount;
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
    				
    				//Citation containing abstracts alone are processed
    				if(!arrFileContents[4].equals("Abstract-")) {	
    					pubmedID = arrFileContents[0].substring(5);
    					title = arrFileContents[3].substring(6);
    					citationAbstract = arrFileContents[4].substring(9);
    					
    					//Abbreviation resolution and sentence segmentation for abstract
    					ArrayList<String> abstractSentences = abbreviationResolver.resolveAbbreviation(citationAbstract);
    					
    					//sentence segmentation for title
    					ArrayList<String> titleSentences = abbreviationResolver.segmentSentences(title);
    					
    					//merge abstract and title sentences
    					sentences = new ArrayList<String>(titleSentences);
    					sentences.addAll(abstractSentences);
    				}
    				
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
