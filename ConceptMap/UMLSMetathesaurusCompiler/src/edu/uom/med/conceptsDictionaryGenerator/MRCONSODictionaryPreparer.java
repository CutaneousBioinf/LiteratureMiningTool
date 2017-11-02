package edu.uom.med.conceptsDictionaryGenerator;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/** 
 * The MRCONSODictionaryPreparer program implements an application that constructs a list of 
 * medical concepts from UMLS Metathesaurus. Each concept is defined by the concept name and a 
 * unique identifier for concepts (CUI). We also include the semantic type from UMLS MetaMap
 * to define the unique semantic type identifier (TUI) of the concept. 
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */

public class MRCONSODictionaryPreparer {
	
	/**
	 * Method to generate the dictionary of concepts from UMLS Metathesaurus and UMLS MetaMap
	 * 
	 * @throws IOException
	 * @param
	 * @return
	 */
	
	public void conceptsDictionaryPreparer() throws IOException {
		
		System.out.println("Preparing the dictionary of concepts from UMLS Metathesaurus and UMLS MetaMap....");
		
		//Reading the processed file from MRSTY.RRF
		BufferedReader in1 = new BufferedReader(new FileReader("resources/secondaryDictionaries/mrsty.cuituiGroup2016AA.txt"));
		ArrayList<String> mrsemType = new ArrayList<String>();
		while(in1.ready()){
			String line = in1.readLine();
			mrsemType.add(line);
		}
		in1.close();
		
		//Reading TUI from UMLS MetaMap
		BufferedReader in2 = new BufferedReader(new FileReader("resources/secondaryDictionaries/semgroup_tui2013.txt"));
		ArrayList<String> semgrp = new ArrayList<String>();
		while(in2.ready()){
			String line = in2.readLine();
			semgrp.add(line);
		}
		in2.close();
		
		//Reading the processed file from MRCONSO.RRF
		BufferedReader in = new BufferedReader(new FileReader("resources/secondaryDictionaries/mrconso.preferredVocabularies2016AA"));
		PrintWriter pwr = new PrintWriter("resources/secondaryDictionaries/umls.metathesaurus.vocabulary2016AA");
		PrintWriter pwr1 = new PrintWriter("resources/secondaryDictionaries/missedVocabulary2016AA.txt");
		
		int count=0;
		String cui="", cui_tui="", semGroup="";
		
		while(in.ready()){
			String line = in.readLine();
			String[] splits=line.split("\\|");
			String term = splits[14];
			cui = splits[0];
			
			//remove ending semantic tag such as (disorder) or [Ambiguity] for better matching
			if(term.endsWith(")")&&term.matches(".+\\s+\\(\\S+\\)"))  term=term.substring(0, term.indexOf("("));
			else if(term.endsWith("]")&&term.matches(".+\\s+\\[\\S+\\]")) term=term.substring(0, term.indexOf("["));
		
			if(term.startsWith("\\(") && term.endsWith("\\]")) continue; 
			else if(term.startsWith("\\[") && term.endsWith("\\]")) continue;
			else {
				//normalized tokens
				String norm = term.toLowerCase(); 
				if(norm.contains(" ")) norm = norm.replaceAll(" ", "\t");
				if(norm.contains(",")) norm = norm.replaceAll(",", " ");
				if(norm.contains("-")) norm = norm.replaceAll("-", " ");
				if(norm.contains("\\(")) norm = norm.replaceAll("\\(", "");
				if(norm.contains("\\)")) norm = norm.replaceAll("\\)", "");
			
				//get TUI
				for(String mr : mrsemType) {
					if(mr.startsWith(cui)) {
						cui_tui = mr;
						break;
					}
				}
			
				//get sematic group
				ArrayList<String> cuiTuiList=null; 
				if(cui_tui.contains(";")) {
					String[] arrcui_tui = cui_tui.split(";");
					cuiTuiList = new ArrayList<String>(Arrays.asList(arrcui_tui));
				}
				if(!cui_tui.contains(";")){
					cuiTuiList = new ArrayList<String>();
					cuiTuiList.add(cui_tui);
				}
				semGroup="";
				for(String eachCuiTui : cuiTuiList) {
					String[] arrCuiTui = eachCuiTui.split("_");
					if(arrCuiTui.length>1) {
						String tui = arrCuiTui[1];
						for(String se : semgrp) {
							if(se.contains("|"+tui+"|")) {
								String[] arrSe = se.split("\\|");
								if(semGroup.isEmpty()) {
									semGroup = arrSe[0];
								}
								else if (!semGroup.contains(arrSe[0])){
									semGroup = semGroup.concat(";").concat(arrSe[0]);
								}
							}
						}
					}
					else {
						pwr1.println("CUI_TUI_List"+cuiTuiList);
					}
				}
				
				term = term.trim();
				if(!norm.isEmpty() && !term.isEmpty() && !semGroup.isEmpty() && !cui_tui.isEmpty()) {
					pwr.println(norm+"|"+term+"||"+semGroup+"|"+cui_tui);
					System.out.println(norm+"|"+term+"||"+semGroup+"|"+cui_tui);
				}
				else {
					pwr1.println(norm+"|"+term+"||"+semGroup+"|"+cui_tui);
				}	
				norm = term = semGroup = cui_tui = "";
			
				count++;
				if(count%1000==0) System.out.println("Processed " + count + " concepts so far...");
			}	
		}
		in.close();
		pwr.close();
		pwr1.close();
		
		System.out.println("Total number of records: "+count);
	}
	
}
