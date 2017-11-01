package edu.uom.med.geneDrugMapper;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class StopwordsRemover identifies and removes the stopwords present in a sentence
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class StopwordsRemover {
	
	/**
	 * Method gets a list of stopwords from a file
	 * 
	 * @return
	 */
	
	public ArrayList<String> getStopwords() {
		String stopword="";
		ArrayList<String> stopWords = new ArrayList<String>();
		try {
			FileReader fr = new FileReader("resources/lookup/stopwords_list.txt");
			BufferedReader br = new BufferedReader(fr);
			while((stopword = br.readLine()) != null) {
				stopWords.add(stopword.toLowerCase());
			}
			br.close();
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return stopWords;
	}
	
	/**
	 * Methods removes the stopwords by exact string matching approach
	 * 
	 * @param sentence
	 * @param stopWords
	 * @return
	 */
	
	public String removeStopwords(String sentence, ArrayList<String> stopWords) {
		String text1="";
		String[] arrText = sentence.split(" ");
		for(String t : arrText) {
			if(!stopWords.contains(t.toLowerCase())) {
				if(text1.isEmpty()) {
					text1 = t;
				}
				else {
					text1 = text1.concat(" ").concat(t);
				}
			}
		}
		
		return text1;
	}
	
}