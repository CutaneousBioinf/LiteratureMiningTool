package edu.uom.med.pathwayNormalizer;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Program applies a set of normalizig rules tp pathway names to identify the    
 * duplicates. It excludes these duplicates while writing to a file.
 *
 * 
 * @author Kalpana Raja
 *
 */

public class PathwayNameNormalizer {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="", line1="";
		
		String arg1 = args[0]; //input_file
		String arg2 = args[1]; //output_file
		
		ArrayList<String> input = new ArrayList<String>();
		Set<String> input1 = new LinkedHashSet<String>();
		Set<String> duplicates = new LinkedHashSet<String>();
		Set<String> dup = new LinkedHashSet<String>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				if(line.startsWith("Gene_symbol")) continue;
				input.add(line);

				//Rule 0: Presence of different indexes of white spaces (e.g. aabb cc vs. aa bb cc) 
				line1 = line;
				if(line1.contains("-")) line1 = line1.replaceAll("-", "");
				if(line1.contains("("))  {
					line1 = line1.replaceAll(" \\(", "");	
					line1 = line1.replaceAll("\\)", ""); 
				}
				line1 = line1.replaceAll("/", "");
				line1 = line1.replaceAll("  ", "");
				line1 = line1.replaceAll("   ", "");
				line1 = line1.replaceAll(" ", "");
				if(!input1.add(line1)) dup.add(line);
				else input1.add(line1);
			}
			
			//Other rules
			duplicates = performNormalization(input);
			for(String eachDup : dup) {
				if(!duplicates.contains(eachDup)) duplicates.add(eachDup);
			}

			//write to file
			for(String eachInput : input) {
				if(!duplicates.contains(eachInput)) {
					bw.append(eachInput);
					bw.append("\n");
				}
			}
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
	public static Set<String> performNormalization(ArrayList<String> input) {
		String eachInputNorm="";
		int outputCount=0, count=0;
		Set<String> duplicates = new LinkedHashSet<String>();
		ArrayList<String> output = new ArrayList<String>();
		
		for(String eachInput : input) {
			count++;
			//if(count==1000) break;
			if(count%100==0) System.out.println("count: " + count);

			//Is function?
			if(eachInput.endsWith(":GO")) continue;

			//Rule 1: Presence of hyphen, delimiters, slash and extra white space
			output = rule1(eachInput, input);
			if(output.size()!=0) {
				for(String eachOutput : output) {
					if(!eachInput.equals(eachOutput)) {
						outputCount = Collections.frequency(input, eachOutput);
						if(outputCount>=1) duplicates.add(eachOutput);
					}
				}
			}			
			eachInputNorm = output.get(0);
			
			//Rule 2: Presence of parenthesis
			output = rule2(eachInputNorm, input);
			if(output.size()!=0) {
				for(String eachOutput : output) {
					if(!eachInput.equals(eachOutput)) {
						outputCount = Collections.frequency(input, eachOutput);
						if(outputCount>=1) duplicates.add(eachOutput);
					}
				}
			}
			
			//Rule 3: Presence of extra word(s) i.e. positive/negative
			if(eachInput.contains("positive") || eachInput.contains("negative")) {
				output = rule3(eachInput, input);
				if(output.size()!=0) {
					for(String eachOutput : output) {
						if(!eachInput.equals(eachOutput)) {
							outputCount = Collections.frequency(input, eachOutput);
							if(outputCount>=1) duplicates.add(eachOutput);
						}
					}
				}
			}
			
			//Rule 4: Presence of extra word or character (i.e. '+' as in ca2+ vs. ca2)
			if(eachInput.contains("+")) {
				output = rule4(eachInput, eachInputNorm, input);
				for(String eachOutput : output) {
					if(!eachOutput.equals("NONE")) {
						duplicates.add(eachOutput);
					}
				}
				
			}
		}
		
		return duplicates;
	}
	
	public static ArrayList<String> rule1(String eachInput, ArrayList<String> input) {
		String eachOutput="", gene="", pathway="";
		
		ArrayList<String> output = new ArrayList<String>();
		
		String[] record = eachInput.split("\t");
		gene = record[0];
		pathway = record[1];
		
		//Rule 1a: Replace hyphen with space
		if(pathway.contains("-")) { 
			boolean flag = false;
			
			pathway = pathway.replaceAll("-", " ");
			String norm = gene + "\t" + pathway;
			int frequency = Collections.frequency(input, norm);
			if(frequency>=1) flag = true;
				
			//Rule 1b: Remove hyphen
			if(!flag) {
				pathway = record[1];
				pathway = pathway.replaceAll("-", ""); 
				pathway = pathway.replaceAll("  ", " ");
			}
		}
		
		//Rule 1c: Remove delimiters
		Pattern pattern = Pattern.compile("[\n\t\r,;:!_&^=\\|\"\']");
		Matcher matcher0 = pattern.matcher(pathway);
		while(matcher0.find()) {	
			pathway = pathway.replaceAll(matcher0.group().toString(), "");
		}
		
		//Rule 1d: Replace forward slash with white space
		if(pathway.contains("/")) { pathway = pathway.replaceAll("/", " "); }
		
		//Rule 1e: Remove extra white space between words
		if(pathway.contains("   ")) { pathway = pathway.replaceAll("   ", " "); }
		if(pathway.contains("  ")) { pathway = pathway.replaceAll("  ", " "); }

		output.add(gene + "\t" + pathway);

		return output;
	}
	
	public static ArrayList<String> rule2(String eachInputNorm, ArrayList<String> input) {
		String eachOutput="", gene="", pathway="", p1="", p11="";
		boolean flag = false, flag1 = false;
	
		ArrayList<String> output = new ArrayList<String>();
		
		String[] record = eachInputNorm.split("\t");
		gene = record[0];
		pathway = record[1];

		//performs only for pathway names with upto 2 parenthesis
		if(pathway.contains(")")) {
			int o = pathway.replaceAll("\\)", "").length();
			if(pathway.length()-o==1 || pathway.length()-o==2) flag = true;
		}

		if(flag) {
			if(pathway.contains(" (")) {
				String s="";
				p11 = pathway;
				
				//removal of parenthesis and characters
				while(p11.contains(")")) {
					s = p11.substring(p11.indexOf("("), p11.indexOf(")")+1);
					p11 = p11.replaceFirst(s, "");
					p11 = p11.replaceFirst("\\(", "");	
					p11 = p11.replaceFirst("\\)", "");
				}
				p11 = p11.replaceAll("  ", " ");

				//removal of parenthesis only
				pathway = pathway.replaceAll("\\(", "");
				pathway = pathway.replaceAll("\\)", "");
				p1 = pathway;
			}
			else {
				p11 = pathway;
				p1 = pathway;
			}
			
			//Duplicates?
			String norm = gene + "\t" + p11.trim();
			int frequency = Collections.frequency(input, norm);
			if(frequency>=1) {
				flag1 = true;
				output.add(norm);
			}
			
			norm = gene + "\t" + p1.trim();
			frequency = Collections.frequency(input, norm);
			if(frequency>=1) {
				flag1 = true;
				output.add(norm);
			}
			
			if(!flag1) {
				output.add(eachInputNorm);
			}
		}	
					
		return output;
	}
	
	public static ArrayList<String> rule3(String eachInput, ArrayList<String> input) {
		String eachOutput="", gene="", pathway="", norm="";
		boolean flag = false;

		ArrayList<String> output = new ArrayList<String>();
		
		String[] record = eachInput.split("\t");
		gene = record[0];
		pathway = record[1];
		
		if(pathway.startsWith("positive")){
			norm = pathway.substring("positive".length()+1);
			eachOutput = gene + "\t" + norm.trim();
			int frequency = Collections.frequency(input, norm);
			if(frequency>=1) flag = true;
			
			if(!flag) {
				norm = pathway.substring("positive regulation of".length()+1);
				output.add(gene + "\t" + norm.trim());
			}
		}
		else if(pathway.startsWith("negative")){
			norm = pathway.substring("negative".length()+1);
			eachOutput = gene + "\t" + norm.trim();
			int frequency = Collections.frequency(input, norm);
			if(frequency>=1) flag = true;
			
			if(!flag) {
				norm = pathway.substring("negative regulation of".length()+1);
				output.add(gene + "\t" + norm.trim());
			}
		}
		else output.add(eachInput);
		
		return output;
	}
	
	public static ArrayList<String> rule4(String eachInput, String eachInputNorm, ArrayList<String> input) {
		String eachOutput="", gene="", otherGene="", pathway="", pathwayNorm="", otherPathway="";

		ArrayList<String> output = new ArrayList<String>();
		
		String[] record = eachInput.split("\t");
		gene = record[0];
		pathway = record[1];
		
		String[] record1 = eachInputNorm.split("\t");
		pathwayNorm = record1[1];
		String arrPathwayNorm[] = pathwayNorm.split(" ");
		
		for(String each : input) {
			if(each.endsWith(":GO")) continue;
			if(each.equals(eachInput)) continue;
			
			String[] record2 = each.split("\t");
			otherGene = record2[0];
			otherPathway = record2[1];
			String arrOtherPathway[] = otherPathway.split(" ");

			//Set intersection to find the extra word
			ArrayList<String> extrawords = new ArrayList<String>();
			Set<String> intersection = new LinkedHashSet<String>(Arrays.asList(arrPathwayNorm));
			intersection.retainAll(Arrays.asList(arrOtherPathway));
			if(intersection.size() ==0) continue;
			for(String eachWord : arrPathwayNorm) {
				if(intersection.contains(eachWord)) continue;
				extrawords.add(eachWord);				
			}
			
			//Match pathway names with extrawords
			if(!extrawords.isEmpty()) {
				//put the word in the middle by knowing the index and see whether the pathway names are the same
				int extrawordIndex = 0;
				String extraword="";
				if(extrawords.size() == 1) {
					extraword = extrawords.get(0);
					
					//get the index of extraword
					if(pathwayNorm.contains(extraword)) {
						if(extraword.length()>1) extrawordIndex = pathwayNorm.indexOf(extraword);
						else extrawordIndex = pathwayNorm.indexOf(" " + extraword + " ")+1;
					}
										
					//positive/negative as extraword, which is present in the middle of the pathway name -- This is a special rule
					if(extraword.equals("positive") || extraword.equals("negative")) {
						if(extrawordIndex > 0) {
							otherPathway = new StringBuffer(otherPathway).insert(extrawordIndex, extraword+" ").toString();
							if(pathwayNorm.equals(otherPathway)) eachOutput = otherGene + "\t" + otherPathway;
						}
					}

					//extra word is very similar to one of the words present in both pathway names -- This is a special rule
					//Example: ca2+ vs. ca2
					char c = extraword.charAt(extraword.length()-1);
					if(c == '+') {
						if(pathwayNorm.contains(extraword)) {
							String s = otherPathway.replaceAll(extraword.substring(0, extraword.length()-1), extraword);
							if(pathwayNorm.equals(s)) {
								if(gene.equals(otherGene)) eachOutput = otherGene + "\t" + otherPathway;
							}
						}
					}
				}
			}
			else eachOutput = "NONE";
			output.add(eachOutput);
		}
		
		return output;
	}

}

