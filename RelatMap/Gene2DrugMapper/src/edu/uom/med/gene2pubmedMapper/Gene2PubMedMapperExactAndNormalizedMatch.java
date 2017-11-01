package edu.uom.med.gene2pubmedMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uom.med.gene2pubmedMapper.Gene2PubMedMapperExactAndNormalizedMatch.Pair;
import edu.uom.med.geneDrugMapper.StopwordsRemover;

/**
 * Project maps gene name, official symbol or gene ID present in PubMed abstracts. We use a simple string matching
 * approach for mapping. However, the authors mention gene names in various morphological forms and thus, simple
 * string matching approach will not be suitable always. We implement a set of normalization rules for gene name
 * normalization prior to mapping in the sentences. The gene2pubmed resource from NCBI is used to get gene 
 * annotations for each PubMed abstracts and mapping is carried out based on this annotation
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class Gene2PubMedMapperExactAndNormalizedMatch {
	
	//global attributes
	static GeneNameNormalizerAndMatcher geneNameNormalizerAndMatcher = new GeneNameNormalizerAndMatcher();
	static StopwordsRemover stopwordsRemover = new StopwordsRemover();
	static LinkedHashMap<String, String> UKAndUS_Spelling = new LinkedHashMap<String, String>();
	static ArrayList<String> ukspelling = new ArrayList<String>();
	static ArrayList<String> stopWords = new ArrayList<String>();
	static ArrayList<String> romanNumerals = new ArrayList<String>();
	static ArrayList<String> genericAcronyms = new ArrayList<String>();
	
	/**
	 * Program execution starts here
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
		String arg2 = args[1]; //INPUT_FILE2 -- pubmedSentences (multiple jobs - 4 more input files are there)
		String arg3 = args[2]; //OUTPUT_FILE1 -- pubmedSentences_genesMapped
		String arg4 = args[3]; //OUTPUT_FILE2 -- mapping_count
		
		stopWords = stopwordsRemover.getStopwords();
		
		try { 
			FileReader fr = new FileReader("resources/lookup/UK_and_US_Spelling_Equivalents.txt");
			BufferedReader br = new BufferedReader(fr);
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				UKAndUS_Spelling.put(arrLine[0].trim(), arrLine[1].trim());
				ukspelling.add(arrLine[0].trim());
			}
			br.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		try { 
			FileReader fr = new FileReader("resources/lookup/roman_numerals.txt");
			BufferedReader br = new BufferedReader(fr);
			while((line = br.readLine()) != null) {
				romanNumerals.add(line.trim());
			}
			br.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
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
					bw.append("\n");
		    		
		    		bw1.append(mappingResult); //pmid, genecount, mappedgenecount
		    		bw1.append("\n");
		    		
		    		sentences.clear();
		    		sentences.add(line);
		    		prepmid=pmid;
		    	}
		    		
		    	count++;
				//if(count==1000) break;
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
	 * Method for gene mapping first uses exact string matching approach and then uses normalized string matching
	 * approach, if the exact matching fails.
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
    			//New Rule 1: Removal of Gene Symbol as Roman alphabets
    			if(!romanNumerals.contains(symbol)) {
    				if(eachSentence.contains(" "+symbol+" ") || eachSentence.contains("\t"+symbol+" ") || 
    						eachSentence.endsWith(" "+symbol+".") || eachSentence.contains("("+symbol+")") ||
    						eachSentence.contains("("+symbol+" ") || eachSentence.contains(" "+symbol+")")) {
    					if(!genes.contains(symbol)) genes.add(symbol);
    					if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
    					if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
    				}
    			}
				if(synonyms.size()>0) {
					for(String eachSynonym : synonyms) {
						//New Rule 1: Removal of Gene synonym as Roman alphabets
						if(!romanNumerals.contains(eachSynonym)) {
							//New Rule 2: Number of characters in gene synonym should be 2-7
							if(eachSynonym.length()>=2 && eachSynonym.length()<=7) {
								if(eachSentence.contains(" "+eachSynonym+" ") || eachSentence.contains("\t"+eachSynonym+" ") || 
										eachSentence.endsWith(" "+eachSynonym+".") || eachSentence.contains("("+eachSynonym+")") ||
										eachSentence.contains("("+eachSynonym+" ") || eachSentence.contains(" "+eachSynonym+")")) {
									if(!genes.contains(eachSynonym)) genes.add(eachSynonym);
									if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
									if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
								}
							}
						}
					}
				}
				
				//normalized mapping
				//Normalization Rule 1: Normalization of case
				String geneName1 = geneName.toLowerCase().trim();
				String eachSentence1 = eachSentence.toLowerCase().trim();
				if(eachSentence1.contains(" "+geneName1+" ") || eachSentence1.contains("\t"+geneName1+" ") || 
						eachSentence1.endsWith(" "+geneName1+".") || eachSentence1.contains("("+geneName1+")") ||
						eachSentence1.contains(geneName1)) {
					if(!genes.contains(geneName)) genes.add(geneName);
					if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
					if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
				}
				
				//normalized mapping of gene names only
				if(!genes.contains(geneName)) {
					String normalizedGeneName = geneNameNormalizerAndMatcher.performNormalizedGeneMapping(geneName1, eachSentence1, stopWords, UKAndUS_Spelling, ukspelling, genericAcronyms);
					if(!normalizedGeneName.isEmpty()) {
						if(!genes.contains(geneName)) genes.add(geneName);
						if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
						if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
					}
				}
				
				//normalized mapping of gene symbol and synonyms present in text
				//New Normalization Rule 1: Removal of hyphen
				eachSentence1 = eachSentence.replaceAll("-", "");
				if(!romanNumerals.contains(symbol)) {
					if(eachSentence1.contains(" "+symbol+" ") || eachSentence1.contains("\t"+symbol+" ") || 
							eachSentence1.endsWith(" "+symbol+".") || eachSentence1.contains("("+symbol+")")) {
						if(!genes.contains(symbol)) genes.add(symbol);
						if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
						if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
					}
				}
				if(synonyms.size()>0) {
					for(String eachSynonym : synonyms) {
						if(!romanNumerals.contains(eachSynonym)) {
							if(eachSynonym.length()>=2 && eachSynonym.length()<=7) {
								if(eachSentence1.contains(" "+eachSynonym+" ") || eachSentence1.contains("\t"+eachSynonym+" ") || 
										eachSentence1.endsWith(" "+eachSynonym+".") || eachSentence1.contains("("+eachSynonym+")")) {
									if(!genes.contains(eachSynonym)) genes.add(eachSynonym);
									if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
									if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
								}
							}
						}
					}
				}
				
				//New Normalization Rule 2: First letter alone is Uppercase
				String symbol1 = symbol.substring(1).toLowerCase();
				symbol1 = symbol.charAt(0)+symbol1;
				if(!romanNumerals.contains(symbol)) {
					if(eachSentence1.contains(" "+symbol1+" ") || eachSentence1.contains("\t"+symbol1+" ") || 
							eachSentence1.endsWith(" "+symbol1+".") || eachSentence1.contains("("+symbol1+")")) {
						if(!genes.contains(symbol)) genes.add(symbol1);
						if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
						if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
					}
				}
				if(synonyms.size()>0) {
					for(String eachSynonym : synonyms) {
						if(!romanNumerals.contains(eachSynonym)) {
							if(eachSynonym.length()>=2 && eachSynonym.length()<=7) {
								String synonym1 = eachSynonym.substring(1).toLowerCase();
								synonym1 = eachSynonym.charAt(0)+synonym1;
								if(eachSentence1.contains(" "+synonym1+" ") || eachSentence1.contains("\t"+synonym1+" ") || 
										eachSentence1.endsWith(" "+synonym1+".") || eachSentence1.contains("("+synonym1+")")) {
									if(!genes.contains(eachSynonym)) genes.add(synonym1);
									if(!genesSymbol.contains(symbol)) genesSymbol.add(symbol);
									if(!genesMapped.contains(symbol)) genesMapped.add(symbol);
								}
							}
						}
					}
				}
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
	 * Class is meant to return multiple return values. We adopted this from C++ programming language
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

