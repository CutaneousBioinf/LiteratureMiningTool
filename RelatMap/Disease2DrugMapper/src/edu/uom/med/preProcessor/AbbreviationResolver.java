package edu.uom.med.preProcessor;


import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java class AbbreviationResolver implements the algorithm to identify and convert the 
 * abbreviations to the original expanded name. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class AbbreviationResolver {
	
	//global attributes
	ArrayList<String> abstractSentences1 = new ArrayList<String>();
	
	/**
	 * Method identifies the first declaration of the abbreviation and retrieves its original
	 * expanded name. Then it replaces the abbreviation with the original name in the 
	 * successive sentences from the same PubMed abstract. The entire algorithm works on the
	 * assumption that the abbreviations are declared within a pair of braces when defined for 
	 * the first time.
	 * 
	 * @param abstractLine
	 * @return
	 */
	
	public ArrayList<String> resolveAbbreviation(String abstractLine) {
		String abbreviation="";
		ArrayList<String> preProcessedSentences = new ArrayList<String>();
		ArrayList<String> sentences = segmentSentences(abstractLine);
		
		for(int i=0; i<sentences.size(); i++) { 
			String eachSentence = sentences.get(i);
			eachSentence = eachSentence.replaceAll("/", " / ");
			String[] sent = eachSentence.split(" ");
			for(String sentTerm : sent) {
				if(sentTerm.endsWith(".") || sentTerm.endsWith(",")) {
					sentTerm = sentTerm.substring(0, sentTerm.length()-1);
				}
				
				if(sentTerm.startsWith("(") && sentTerm.endsWith(")")) { 
					eachSentence = eachSentence.replace(sentTerm, "");
					eachSentence = eachSentence.replace("  ", " ");
				}
				
				Pattern p = Pattern.compile("[A-Z]");
				Matcher m = p.matcher(sentTerm);
				if(m.find()) {
					abbreviation = sentTerm.trim();
				}
				
				if(abbreviation.matches("(^[A-Za-z])")) { abbreviation = ""; }
				if(abbreviation.matches("[-+*]")) { abbreviation = ""; }
				if(abbreviation.matches("\\*(.+?)")) { abbreviation = ""; }
					
				if(abbreviation.contains("(+)")) { abbreviation = ""; }
				if(abbreviation.contains("(*)")) { abbreviation = ""; }
				if(abbreviation.contains("+")) { abbreviation = ""; }
				if(abbreviation.matches("(.+?)-(.+?)")) { abbreviation = ""; }
				if(abbreviation.matches("(.+?)\\((.+?)") || abbreviation.matches("(.+?)\\)") || abbreviation.matches("\\((.+?)")) {
					abbreviation = "";
				}
								
				if(!abbreviation.isEmpty()) {
					eachSentence = replaceAbbreviation(eachSentence, abbreviation, sentences);
				}
				
				//added abbreviations
				if(eachSentence.contains("HTx")) { eachSentence = eachSentence.replaceAll("HTx", "heart transplantation"); }
			}	
			preProcessedSentences.add(eachSentence);
		}
		
		return preProcessedSentences;
	}

	/**
	 * Method takes a PubMed abstract as input and splits its sentences
	 * 
	 * @param citationAbstract
	 * @return
	 */
	
	public ArrayList<String> segmentSentences(String citationAbstract) {
		ArrayList<String> abstractSentences = new ArrayList<String>();
				
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		iterator.setText(citationAbstract);
		int lastIndex = iterator.first();
		
		while (lastIndex != BreakIterator.DONE) {
			int firstIndex = lastIndex;
			lastIndex = iterator.next();
			if (lastIndex != BreakIterator.DONE) {
				String sent = citationAbstract.substring(firstIndex, lastIndex);
				if(sent.startsWith("<AbstractText ")) {
					sent = sent.substring(sent.indexOf("\">")+2);
				}
				abstractSentences.add(sent);
			}
		}
		
		return abstractSentences;
	}
	
	/**
	 * Method replaces abbreviation with original expanded name
	 * 
	 * @param sentence
	 * @param abbreviation
	 * @param PMIDAbstract
	 * @return
	 */
	
	public String replaceAbbreviation(String sentence, String abbreviation, 
			ArrayList<String> PMIDAbstract) {
		int start;
		String abb = "(".concat(abbreviation).concat(")"), abbTerm, abbreviationTerm="";
		
		for(String pmidEntry : PMIDAbstract) {
			if(pmidEntry.contains(abb)) {
				String sub = pmidEntry.substring(0, pmidEntry.indexOf(abb));
				String[] arrsub = sub.split(" ");
				if(arrsub.length-abbreviation.length() >= 0) { start = arrsub.length-abbreviation.length(); }
				else { start=0; }
				String[] arrsub1 = Arrays.copyOfRange(arrsub, start, arrsub.length);
				StringBuffer sb = new StringBuffer();
				for(String term : arrsub1) {
					sb.append(term+" ");
				}
				abbTerm = sb.toString();
				abbreviationTerm = findBestLongForm(abbreviation, abbTerm);
				
				if(abbreviationTerm != null) {
					if(abbreviationTerm.contains("-")) {
						abbreviationTerm = abbreviationTerm.replaceAll("-", " ");
					}
										
					sentence = sentence.replace(abbreviation, abbreviationTerm.trim());
					sentence = sentence.replaceAll(abb, "");
				}
			}
		}
		
		return sentence;
	}
	
	/**
	 * Method finds the best number of preceding words to the abbreviation that contains the 
	 * original expanded name.
	 * 
	 * @param abbreviation
	 * @param abbTerm
	 * @return
	 */
	
	public String findBestLongForm(String abbreviation, String abbTerm) {
		int sIndex, lIndex=0;
		char currChar;
				
		sIndex = abbreviation.length()-1;
		lIndex = abbTerm.length()-1;
		for(; sIndex >=0; sIndex--) {
			currChar = Character.toLowerCase(abbreviation.charAt(sIndex));
			if(!Character.isLetterOrDigit(currChar)) 
				continue;
			while (((lIndex >= 0) && (Character.toLowerCase(abbTerm.charAt(lIndex)) != currChar)) || 
					((sIndex == 0) && (lIndex > 0) && (Character.isLetterOrDigit(abbTerm.charAt(lIndex - 1))))) 
				lIndex--;
			if(lIndex<0)
				return null;
			lIndex--;
		}
		lIndex = abbTerm.lastIndexOf(" ", lIndex)+1;
		
		return abbTerm.substring(lIndex);
	}
	
}
