package edu.uom.med.drugsDictionaryGenerator;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Program to remove stop words present in an input sentence. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class StopwordsRemover {
	
	/**
	 * Method to collect the list of stopwords from a file.
	 * 
	 * @return
	 */
	
	public ArrayList<String> getStopwords() {
		String stopword="";
		ArrayList<String> stopWords = new ArrayList<String>();
		try {
			FileReader fr = new FileReader("resources/lookup/stopwords_list.txt");
			//FileReader fr = new FileReader("/net/psoriasis/home/rkalpana/Java_Applications/Input/GeneDrugMapping/stopwords_list.txt");
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
	 * Method to identify and remove the stopwords present in an input sentence.
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