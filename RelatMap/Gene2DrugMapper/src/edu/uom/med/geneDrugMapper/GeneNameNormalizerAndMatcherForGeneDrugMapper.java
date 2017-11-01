package edu.uom.med.geneDrugMapper;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class GeneNameNormalizerAndMatcherForGeneDrugMapper {
	
	//global attributes
	StopwordsRemover stopwordsRemover = new StopwordsRemover();
	
	/**
	 * Methods wraps the normalization rules declared in other methods
	 * 
	 * @param geneName1
	 * @param eachSentence1
	 * @param stopWords
	 * @param UKAndUS_Spelling
	 * @param ukspelling
	 * @return
	 */
	
	public String performNormalizedGeneMapping(String geneName1, String eachSentence1, ArrayList<String> stopWords,
			LinkedHashMap<String, String> UKAndUS_Spelling, ArrayList<String> ukspelling) {
		String normalizedGeneName="";
		
		if(eachSentence1.contains(" ")) { //check whether it is a proper sentence
			String[] arrEachSentence1 = eachSentence1.split("\t");
		
			eachSentence1 = arrEachSentence1[1].trim();
		
			//apply normalization rules to gene name and text
			Pair pair = geneNameAndTextNormalizer(geneName1, eachSentence1, stopWords, UKAndUS_Spelling, ukspelling);
			String geneName2 = pair.getGeneName();
			String eachSentence2 = pair.getPMIDSentence();
		
			//apply normalization rules to text only or gene name only
			String eachSentence3 = textNormalizer(geneName2, eachSentence2);
		
			//Rule 9: Removal of all spaces
			//String geneName3 = geneName2;
			geneName2 = geneName2.replaceAll(" ", "");
			eachSentence3 = eachSentence3.replaceAll(" ", "");
	
			if(eachSentence3.contains(geneName2)) normalizedGeneName = geneName2;
		}
		
		return normalizedGeneName;
	}
	
	/**
	 * Methods wraps the normalization rules applicable to both text and gene names
	 * 
	 * @param geneName1
	 * @param eachSentence1
	 * @param stopWords
	 * @param UKAndUS_Spelling
	 * @param ukspelling
	 * @return
	 */
	
	public Pair geneNameAndTextNormalizer(String geneName1, String eachSentence1, ArrayList<String> stopWords,
			LinkedHashMap<String, String> UKAndUS_Spelling, ArrayList<String> ukspelling) {
		//Rule 2: Replacement of hyphen with space
		if(geneName1.contains("-")) { geneName1 = geneName1.replaceAll("-", " "); }
		if(eachSentence1.contains("-")) { eachSentence1 = eachSentence1.replaceAll("-", " "); }
		
		//Rule 3: Elimination of word delimiter features (i.e. semicolon, colon and comma)
		Pattern pattern = Pattern.compile("[\n\t\r,;:!_&^=\\|\"\']");
		Matcher geneNameMatcher = pattern.matcher(geneName1);
		while(geneNameMatcher.find()) {	
			geneName1 = geneName1.replaceAll(geneNameMatcher.group().toString(), "");
		}
		Matcher sentenceMatcher = pattern.matcher(eachSentence1);
		while(sentenceMatcher.find()) {	
			eachSentence1 = eachSentence1.replaceAll(sentenceMatcher.group().toString(), "");
		}
		
		//Rule 4: Removal of stop words
		geneName1 = stopwordsRemover.removeStopwords(geneName1, stopWords);
		eachSentence1 = stopwordsRemover.removeStopwords(eachSentence1, stopWords);
		
		//Rule 5: Removal of contents inside parenthesis
		if(geneName1.contains(")")) {
			geneName1 = geneName1.replaceAll("\\(", "");
			geneName1 = geneName1.replaceAll("\\)", "");
		}
		if(geneName1.contains("]")) {
			geneName1 = geneName1.replaceAll("\\[", "");
			geneName1 = geneName1.replaceAll("\\]", "");
		}
		
		if(eachSentence1.contains(")")) {
			eachSentence1 = eachSentence1.replaceAll("\\(", "");
			eachSentence1 = eachSentence1.replaceAll("\\)", "");
		}
		if(eachSentence1.contains("]")) {
			eachSentence1 = eachSentence1.replaceAll("\\[", "");
			eachSentence1 = eachSentence1.replaceAll("\\]", "");
		}
		
		//Rule 6: Arabic numeral to Roman alphabets
		
		//Rule 7: Removal of extra words --only for gene name
		if(geneName1.contains("human")) { geneName1 = geneName1.replace("human", ""); }
		if(geneName1.contains("gene")) { geneName1 = geneName1.replace("gene", ""); }
		if(geneName1.contains("genes")) { geneName1 = geneName1.replace("genes", ""); }
		if(geneName1.contains("protein")) { geneName1 = geneName1.replace("protein", ""); }
		if(geneName1.contains("proteins")) { geneName1 = geneName1.replace("proteins", ""); }
		if(geneName1.contains("mutant")) { geneName1 = geneName1.replace("mutant", ""); }
		if(geneName1.contains("nutants")) { geneName1 = geneName1.replace("mutants", ""); }
		
		//Rule 8: Replacement of British spelling to American spelling
		String[] arrGeneName1 = geneName1.split(" ");
		for(String eachToken : arrGeneName1) {
			if(!ukspelling.contains(eachToken)) continue;
			
			String usToken = UKAndUS_Spelling.get(eachToken);
			geneName1 = geneName1.replaceAll(eachToken, usToken);
		}
		
		String[] arrEachSentence1 = eachSentence1.split(" ");
		for(String eachToken : arrEachSentence1) {
			if(!ukspelling.contains(eachToken)) continue;
			
			String usToken = UKAndUS_Spelling.get(eachToken);
			eachSentence1 = eachSentence1.replaceAll(eachToken, usToken);
		}
				
		Pair pair = new Pair(geneName1, eachSentence1);
		return pair;
	}
	
	/**
	 * Method wraps the normalization rules applicable only to text
	 * 
	 * @param geneName2
	 * @param eachSentence2
	 * @return
	 */
	
	public String textNormalizer(String geneName2, String eachSentence2) {
		String eachSentence3="", geneName3="", originalGeneMention="", modifiedGeneMention="";
		
		//Rules related to slash
		if(geneName2.endsWith("1") && eachSentence2.contains(geneName2.concat("/2"))) {
			originalGeneMention = geneName2.concat("/2");
			geneName3 = geneName2.replace(geneName2.charAt(geneName2.length()-1), '2');
			modifiedGeneMention = geneName2.concat("/").concat(geneName3);
			eachSentence3 = eachSentence3.replaceAll(originalGeneMention, modifiedGeneMention);
		}
		else if(geneName2.endsWith("a") && eachSentence2.contains(geneName2.concat("/b"))) {
			originalGeneMention = geneName2.concat("/b");
			geneName3 = geneName2.replace(geneName2.charAt(geneName2.length()-1), 'b');
			modifiedGeneMention = geneName2.concat("/").concat(geneName3);
			eachSentence3 = eachSentence3.replaceAll(originalGeneMention, modifiedGeneMention);
		}
		else if(geneName2.endsWith("alpha") && eachSentence2.contains(geneName2.concat("/beta"))) {
			originalGeneMention = geneName2.concat("/beta");
			geneName3 = geneName2.replace("alpha", "beta");
			modifiedGeneMention = geneName2.concat("/").concat(geneName3);
			eachSentence3 = eachSentence3.replaceAll(originalGeneMention, modifiedGeneMention);
		}
		else {
			eachSentence3 = eachSentence2;
		}
		
		return eachSentence3;
	}
	
	/**
	 * Method converts a string variable to double and returns true / false value
	 * 
	 * @param str
	 * @return
	 */
	
	public static boolean isNumeric(String str) {
	  try {  
	    double d = Double.parseDouble(str);  
	  } catch(NumberFormatException nfe) {  
	    return false;  
	  }  
	  
	  return true;  
	}
	
	/**
	 * Class meant to return multiple values. We adopted this from C++ programming language
	 * 
	 * @author Kalpana Raja
	 *
	 */
	
	public static class Pair {
		private String geneName1;
		private String eachSentence1;
		Pair(String geneName1,String eachSentence1) {
			this.geneName1 = geneName1;
			this.eachSentence1 = eachSentence1;
		}
		public String getGeneName() {
			return geneName1;
		}
		public String getPMIDSentence() {
			return eachSentence1;
		}
	}
	
}

