package edu.uom.med.geneDrugMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import edu.mayo.nlp.qa.ChemicalAnswerer;

/**
 * The project maps genes and chemicals/drugs present in a sentence from PubMed abstract. We use gene2pubmed
 * resource from NCBI for gene mapping and a customized dictionary of chemicals/drugs, which is compiled from
 * UMLS Metathesaurus, DrugBank and PharmGKB. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class GeneDrugMapper {
	
	//global attributes 
	static ChemicalsAndDrugsMapper chemicalsAndDrugsMapper = new ChemicalsAndDrugsMapper();
	static ChemicalAnswerer chemicalAnswerer = new ChemicalAnswerer("resources/lookup/ChemicalsAndDrugsLexicon");;
	
	static ArrayList<String> chemicalsList = new ArrayList<String>();
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		chemicalsList = getChemicalsList();
		
		String line="", pmid="", sentence="", genesMapped="", geneSymbol="";
		int count=0;
		
		String arg1 = args[0]; //input file -- pubmedSentencesaa_output_genesMapped_output.txt (multiple jobs - 4 more input files are there)
		String arg2 = args[1]; //output file -- pubmedSentencesaa_output_genes_chemicalsAndDrugs_Mapped_output.txt
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
		    InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		    
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		    
		    while((line = br.readLine()) != null) {
		    	
		    	//Chemical and Drug mapping with MedTagger and customized chemicals and drugs dictionary
			    if(line.contains("\t")) {
			    	String[] arrEachMappedSentence = line.split("\t");
			    	
			    	if(arrEachMappedSentence.length != 5) continue;
			    	if(arrEachMappedSentence[3].equals("[]")) continue;
			    
			    	if(!arrEachMappedSentence[3].contains(", ")) { //to consider sentences with one gene mapping
			    		pmid = arrEachMappedSentence[0];
			    		sentence = arrEachMappedSentence[1];
			    		genesMapped = arrEachMappedSentence[2].substring(1,arrEachMappedSentence[2].length()-1);
			    		geneSymbol = arrEachMappedSentence[3].substring(1, arrEachMappedSentence[3].length()-1);
			    		
			    		String drugInfo = chemicalsAndDrugsMapper.performChemicalsAndDrugsMapping(sentence, chemicalsList, chemicalAnswerer);
			    		if(!drugInfo.isEmpty()) {
			    			String[] arrDrugInfo = drugInfo.split("\t");
			    			
			    			//Gene and drug name can't be the same
			    			if(genesMapped.contains(", ") && !arrDrugInfo[0].contains(" | ")) {
			    				String[] genes = genesMapped.split(", ");
			    				boolean flag = false;
			    				for(String eachGene : genes) {
			    					eachGene = eachGene.trim().toLowerCase();
			    					arrDrugInfo[0] = arrDrugInfo[0].trim().toLowerCase();
			    					if(eachGene.equals(arrDrugInfo[0]) || eachGene.contains(arrDrugInfo[0]) ||
			    							arrDrugInfo[0].contains(eachGene)) {
			    						flag = true;
			    						break;
			    					}
			    				}
			    				if(!flag) {
			    					bw.append(pmid+"\t"+sentence+"\t"+genesMapped+"\t"+geneSymbol+"\t"+drugInfo);
			    					bw.append("\n");
			    				}
			    			}
			    			else if(!genesMapped.contains(", ") && arrDrugInfo[0].contains(" | ")) {
			    				String[] drugs = arrDrugInfo[0].split("\\|");
			    				boolean flag = false;
			    				for(String eachDrug : drugs) {
			    					eachDrug = eachDrug.trim().toLowerCase();
			    					String genesMapped1 = genesMapped.trim().toLowerCase(); 
			    					if(eachDrug.equals(genesMapped1) || eachDrug.contains(genesMapped1) ||
			    							genesMapped1.contains(eachDrug)) {
			    						flag = true;
			    						break;
			    					}
			    				}
			    				if(!flag) {
			    					bw.append(pmid+"\t"+sentence+"\t"+genesMapped+"\t"+geneSymbol+"\t"+drugInfo);
			    					bw.append("\n");
			    				}
			    			}
			    			else if(genesMapped.contains(", ") && arrDrugInfo[0].contains(" | ")) {
			    				String[] genes = genesMapped.split(", ");
			    				String[] drugs = arrDrugInfo[0].split("\\|");
			    					
			    				ArrayList<String> genesList = new ArrayList<String>();
			    				for(String eachGene : genes) {
			    					genesList.add(eachGene.trim().toLowerCase());
			    				}
			    					
			    				ArrayList<String> drugsList = new ArrayList<String>();
			    				for(String eachDrug : drugs) {
			    					drugsList.add(eachDrug.trim().toLowerCase());
			    				}
			    					
			    				Set<String> intersect = new TreeSet<String>(genesList);
			    				intersect.retainAll(drugsList);
			    				    
			    				boolean flag = false;
			    				for(String gene : genesList) {
			    					for(String drug : drugsList) {
			    						if(gene.contains(drug) || drug.contains(gene)) {
			    							flag = true;
			    							break;
			    						}
			    					}
			    				}
			    				    
			    				boolean flag1 = false;
			    				for(String drug : drugsList) {
			    					for(String gene : genesList) {
			    						if(gene.contains(drug) || drug.contains(gene)) {
			    							flag1 = true;
			    							break;
			    						}
			    					}
			    				}
			    				    
			    				if(intersect.size()==0 && !flag && !flag1) {
			    					bw.append(pmid+"\t"+sentence+"\t"+genesMapped+"\t"+geneSymbol+"\t"+drugInfo);
			    					bw.append("\n");
			    				}
			    			}
			    			else if(!genesMapped.contains(", ") && !arrDrugInfo[0].contains(" | ")) {
			    				String genesMapped1 = genesMapped.trim().toLowerCase();
			    				arrDrugInfo[0] = arrDrugInfo[0].trim().toLowerCase();
			    				if(!genesMapped1.equals(arrDrugInfo[0]) && !genesMapped1.contains(arrDrugInfo[0])
			    						&& !arrDrugInfo[0].contains(genesMapped1)) {	
			    					bw.append(pmid+"\t"+sentence+"\t"+genesMapped+"\t"+geneSymbol+"\t"+drugInfo);
			    					bw.append("\n");
			    				}
			    			}
			    		}
			    	}
			    
			    	count++;
			    	//if(count%1000==0)System.out.println(count);
			    	//if(count==1000) break;
			    }
		    }
		    		
		    br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time: "+elapsedTime+" milliseconds");
	}
	
	/**
	 * Method to get list of chemicals and drug names in UMLS Metathesaurus
	 * 
	 * @return
	 */
	
	public static ArrayList<String> getChemicalsList() {
		String line="";
		
		try {
			FileReader fr = new FileReader("/net/psoriasis/home/rkalpana/Projects/Complex_Traits_50/Disease_Drug_Mapping/Step_3_disease_drug_mapping/Input/ChemicalsAndDrugsList.txt");
			//FileReader fr = new FileReader("resources/lookup/ChemicalsAndDrugsList.txt");
			BufferedReader br = new BufferedReader(fr);
			while((line = br.readLine())!=null) { 
				chemicalsList.add(line); 
			}
			br.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		return chemicalsList;
	}
	
}

