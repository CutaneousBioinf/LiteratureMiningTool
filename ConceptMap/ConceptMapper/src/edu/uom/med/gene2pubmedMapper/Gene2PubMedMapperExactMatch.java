package edu.uom.med.gene2pubmedMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project maps gene name, official symbol or gene ID present in PubMed abstracts. We use a simple string matching
 * approach for mapping. The gene2pubmed resource from NCBI is used to get gene annotations for each PubMed 
 * abstracts and mapping is carried out based on this annotation.
 * 
 * @author Kalpana Raja
 *
 */

public class Gene2PubMedMapperExactMatch {
	
	/**
	 * Program execution starts from here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="", pmid="", prepmid="", geneCountAndInfo="", geneCount="", geneInfo="";
		int count=0;
		
		LinkedHashMap<String, String> geneInformation = new LinkedHashMap<String, String>();
		ArrayList<String> sentences = new ArrayList<String>();
		
		String arg1 = args[0]; //INPUT_FILE1 --gene2pubmed_human_hasAbstract_1to5GeneMapping_sortedOnPubmed_pubmedGenecountGenelist_pubmedGenecountGenenamelist 
		String arg2 = args[1]; //INPUT_FILE2 -- pubmedSentences
		String arg3 = args[2]; //OUTPUT_FILE1 -- pubmedSentences_genesMapped
		String arg4 = args[3]; //OUTPUT_FILE2 -- mapping_count
		
		try {
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
		    	String[] arrLine = line.split("\t");
		    	geneInformation.put(arrLine[0], arrLine[1]+"\t"+arrLine[2]);
		    }
		    
		    FileInputStream fis = new FileInputStream(arg2);
		    InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		    
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		    
		    FileOutputStream fos1 = new FileOutputStream(arg4);
		    OutputStreamWriter osr1 = new OutputStreamWriter(fos1, "UTF-8");
		    BufferedWriter bw1 = new BufferedWriter(osr1);
		    
		    while((line = br.readLine()) != null) {
		    	String[] arrLine = line.split("\t");
		    	
		    	pmid = arrLine[0];
		    	if(prepmid.isEmpty()) {
		    		sentences.add(line);
		    		prepmid = pmid;
		    	}
		    	else if(pmid.equals(prepmid)) {
		    		sentences.add(line);
		    	}
		    	else if(!pmid.equals(prepmid)) {
		    		geneCountAndInfo = geneInformation.get(prepmid);
		    		String[] arrGeneInfo = geneCountAndInfo.split("\t");
		    		geneCount = arrGeneInfo[0];
		    		geneInfo = arrGeneInfo[1];
			    	Pair pairResult = performGeneMapping(sentences, geneCount, geneInfo, prepmid);
			    	ArrayList<String> mappedSentences = pairResult.getMappedSentences();
					String mappingResult = pairResult.getMappingResult();
					for(String eachMappedSentence : mappedSentences) {
		    			bw.append(eachMappedSentence); 
		    			bw.append("\n");
		    		}
		    		
		    		bw1.append(mappingResult); //pmid, genecount, mappedgenecount for analysis
		    		bw1.append("\n");
		    		
		    		sentences.clear();
		    		sentences.add(line);
		    		prepmid=pmid;
		    	}
		    		
		    	count++;
				//if(count==100) break;
		    }
		    
		    //for last record
		    geneCountAndInfo = geneInformation.get(prepmid);
    		String[] arrGeneInfo = geneCountAndInfo.split("\t");
    		geneCount = arrGeneInfo[0];
    		geneInfo = arrGeneInfo[1];
		    Pair pairResult = performGeneMapping(sentences, geneCount, geneInfo, prepmid);
		    ArrayList<String> mappedSentences = pairResult.getMappedSentences();
			String mappingResult = pairResult.getMappingResult();
			for(String eachMappedSentence : mappedSentences) {
    			//System.out.println(eachMappedSentence);
    			bw.append(eachMappedSentence);
    			bw.append("\n");
    		}
			
			bw1.append(mappingResult);
    		bw1.append("\n");
    		
    		br0.close();
			br.close();
			bw.close();
			bw1.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time: "+elapsedTime+" milliseconds");
	}
	
	/**
	 * Method maps the gene name, official symbol or ID in PubMed sentences.
	 * 
	 * @param sentences
	 * @param geneCount
	 * @param geneInfo
	 * @param prepmid
	 * @return
	 */
	
	public static Pair performGeneMapping(ArrayList<String> sentences, String geneCount, String geneInfo, String prepmid) {
		String symbol="", geneName="", mappingResult="";
		int mappedGeneCount=0;
		ArrayList<String> mappedSentences = new ArrayList<String>();
		ArrayList<String> genes = new ArrayList<String>();
		ArrayList<String> genesSymbol = new ArrayList<String>();
		ArrayList<String> genesMapped = new ArrayList<String>();
		ArrayList<String> geneSymbolSynonymName = new ArrayList<String>();
		
		if(geneInfo.contains(" ### ")) {
			String[] arrGeneInfo = geneInfo.split(" ### ");
			geneSymbolSynonymName = new ArrayList<String>(Arrays.asList(arrGeneInfo));
		}
		else geneSymbolSynonymName.add(geneInfo);
		
		for(String eachSentence : sentences) {
			for(String eachGeneInfo : geneSymbolSynonymName) {
				String[] arrGeneInfo = eachGeneInfo.split("\\$\\$\\$"); //$$$
				symbol = arrGeneInfo[0];
				String[] arrSynonyms = arrGeneInfo[1].split("\\|");
				ArrayList<String> synonyms = new ArrayList<String>(Arrays.asList(arrSynonyms));
				geneName = arrGeneInfo[2];
				
				//Eliminate word delimiter features (i.e. semicolon, colon and comma)
    			Pattern p = Pattern.compile("[,;:!&\"\']");
    			Matcher m = p.matcher(eachSentence);
    			while(m.find()) {	
    				eachSentence = eachSentence.replaceAll(m.group().toString(), "");
    			}
    		
				//exact matching
    			eachSentence = eachSentence.trim();
    			if(eachSentence.contains(" "+symbol+" ") || eachSentence.contains("\t"+symbol+" ") || 
						eachSentence.endsWith(" "+symbol+".") || eachSentence.contains("("+symbol+")") ||
						eachSentence.contains("("+symbol+" ") || eachSentence.contains(" "+symbol+")")) {
					if(!genes.contains(symbol)) genes.add(symbol);
					if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
					if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
				}
				if(synonyms.size()>0) {
					for(String eachSynonym : synonyms) {
						if(eachSentence.contains(" "+eachSynonym+" ") || eachSentence.contains("\t"+eachSynonym+" ") || 
								eachSentence.endsWith(" "+eachSynonym+".") || eachSentence.contains("("+eachSynonym+")") ||
								eachSentence.contains("("+eachSynonym+" ") || eachSentence.contains(" "+eachSynonym+")")) {
							if(!genes.contains(eachSynonym)) genes.add(eachSynonym);
							if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
							if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
						}
					}
				}
				/*String geneName1 = geneName.toLowerCase().trim();
				String eachSentence1 = eachSentence.toLowerCase();
				if(eachSentence1.contains(" "+geneName1+" ") || eachSentence1.startsWith(geneName1+" ") || 
						eachSentence1.endsWith(" "+geneName1+".") || eachSentence1.contains("("+geneName1+")") ||
						eachSentence1.contains(geneName1)) {
					if(!genes.contains(geneName)) genes.add(geneName);
					if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
					if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
				}*/
    		}
		
			String mappedSentence = eachSentence+"\t"+genes+"\t"+genesSymbol+"\t"+genesSymbol.size();
			mappedSentences.add(mappedSentence);
			
			genes.clear();
			genesSymbol.clear();
		}
		
		mappedGeneCount = genesMapped.size();
		mappingResult = prepmid+"\t"+geneCount+"\t"+mappedGeneCount+"\t"+genesMapped;
		
		Pair pairResult = new Pair(mappedSentences, mappingResult);
		
		return pairResult;
	}
	
	/**
	 * Class is meant to return multiple values. We adopted this approach from C++ programming language.
	 * 
	 * @author Kalpana Raja
	 *
	 */
	
	public static class Pair {
		private ArrayList<String> mappedSentences;
		private String mappingResult;
		Pair(ArrayList<String> mappedSentences,String mappingResult) {
			this.mappedSentences = mappedSentences;
			this.mappingResult = mappingResult;
		}
		public ArrayList<String> getMappedSentences() {
			return mappedSentences;
		}
		public String getMappingResult() {
			return mappingResult;
		}
	}
	
}
