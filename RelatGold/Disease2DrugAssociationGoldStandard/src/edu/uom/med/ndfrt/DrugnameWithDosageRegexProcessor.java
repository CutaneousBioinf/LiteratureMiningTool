package edu.uom.med.ndfrt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The drugs in disease-drug associations from NDFRT includes both drug and drug with
 * different types of dosage forms. These have different unique concept identifiers (CUI)  
 * in UMLS Metathesaurus, and our chemical/drug lexicon. Therefore, it is difficult to 
 * consider them as synonyms. Program DrugnameWithDosageRegexProcessor.java is developed
 * to combine such entities as one drug and assign a ID to all of them. 
 *
 *
 * @author Kalpana Raja
 *
 */

public class DrugnameWithDosageRegexProcessor {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- OUTPUT_FILE_sorted (refer Documentation/Disease_drug_association.docx)
		String arg2 = args[1]; //OUTPUT_FILE -- OUTPUT_FILE_NDFRT

		String line="", modifiedID="";
		
		String[] regexPatterns = {"\\s[0-9]+\\.[0-9]+\\s",
					  			  "\\s[0-9]+\\s"
					 			 };
		
		HashMap<String, String> drugAndID = new LinkedHashMap<String, String>();
		ArrayList<String> input = getDosageUnits();
		ArrayList<String> dosageUnits = getDosageUnits();
		ArrayList<String> dosageForms = getDosageForms();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			while((line = br.readLine()) != null) {
				input.add(line);
				
				String[] arrLine = line.split("\t");
				drugAndID.put(arrLine[0].toLowerCase(), arrLine[2]);
			}
			System.out.println(input.size() + "\t" + drugAndID.size());
 
			FileOutputStream fos = new FileOutputStream(arg2);
			OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osr);
			
			for(String eachInput : input) {
				String[] arrLine = eachInput.split("\t");

				Pattern p = Pattern.compile(" / ");
				Matcher m = p.matcher(arrLine[0]);
				if(m.find()) {
					bw.append(eachInput);
					bw.append("\n");
					continue;
				}
				
				for(int i=0; i<regexPatterns.length; i++) {
					Pattern pattern = Pattern.compile(regexPatterns[i]);
					Matcher matcher = pattern.matcher(arrLine[0]);
					if (matcher.find()) {
						int start = matcher.start();
						String drugName = arrLine[0].substring(0, start);
						String dosage = arrLine[0].substring(start).trim();
						
						//confirm the dosage unit presence
						boolean flag = false;
						for(int j=0; j<dosageUnits.size(); j++) {
							if (dosage.contains(dosageUnits.get(j))) {
								flag = true; 
								//break;
							}
						}
						
						//confirm the dosage form
						boolean flag1 = false;
						if(flag) {
							for(int k=0; k<dosageUnits.size(); k++) {
								if (dosage.contains(dosageForms.get(k))) {
									flag1 = true; 
									//break;
								}
							}
						}
						
						if(flag || flag1) {
							//change the ID for drugname
							if(drugName.equalsIgnoreCase("DOXEPIN HCL")) drugName = "doxepin hydrochloride";

							if(drugName.equalsIgnoreCase("24 HR Tacrolimus")) modifiedID = "CD00533160_T200";
							else modifiedID = drugAndID.get(drugName.toLowerCase());
							
							bw.append(arrLine[0] + "\t" + arrLine[1] + "\t" + modifiedID + "\t" + arrLine[3]);
							bw.append("\n");
						}
					}
				}

				if(modifiedID==""){
					bw.append(eachInput);
					bw.append("\n");
				}

				modifiedID="";
			}
			
			br.close();
			bw.close();

		}catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
	public static ArrayList<String> getDosageUnits() {
		ArrayList<String> dosageUnits = new ArrayList<String>();
		
		dosageUnits.add(" MG/MG ");
		dosageUnits.add(" MG/ML ");
		dosageUnits.add(" MG ");
		
		return dosageUnits;
	}
	
	public static ArrayList<String> getDosageForms() {
		ArrayList<String> dosageForms = new ArrayList<String>();
		
		//AD dosage forms
		dosageForms.add("Augmented Topical Cream");
		dosageForms.add("Augmented Topical Gel");
		dosageForms.add("Augmented Topical Lotion");
		dosageForms.add("Augmented Topical Ointment");
		dosageForms.add("Chewable Tablet");
		dosageForms.add("Drug Implant");
		dosageForms.add("Extended Release Oral Capsule");
		dosageForms.add("Injectable Suspension");
		dosageForms.add("Injectable Solution");
		dosageForms.add("Medicated Shampoo");
		dosageForms.add("Oral Capsule");
		dosageForms.add("Oral Solution");
		dosageForms.add("Oral Tablet");
		dosageForms.add("Otic Solution");
		dosageForms.add("PWDR,TOP");
		dosageForms.add("SUSP,ORAL");
		dosageForms.add("Topical Cream");
		dosageForms.add("Topical Gel");
		dosageForms.add("Topical Lotion");
		dosageForms.add("Topical Ointment");
		dosageForms.add("Topical Spray");
		
		return dosageForms;
	}
}
