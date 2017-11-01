package edu.uom.med.gene2pubmedProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program counts number of PubMed IDs for a range of gene mapping (e.g. 1-5 genes per PubMed ID). The main method acts as
 * a wrapper and contains a list of methods for various purposes. Please call the respective method from the main class
 * based on requirement. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class GeneDistributionTester {
	
	//attributes
	static String line="";
	static int count=0, count1=0;
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		///////////filter gene2pubmed_Human file based on PubMed with abstracts information///////////
		String arg1 = args[0]; //INPUT_FILE1 -- PMIDs_from_gene2pubmed_human_hasAbstract
		String arg2 = args[1]; //INPUT_FILE2 -- gene2pubmed_human
		String arg3 = args[2]; //OUTPUT_FILE
		gene2PubmedHumanWithAbstracts(arg1, arg2, arg3);
				
		///////////Count genes for every PMIDs///////////
		/*String arg1 = args[0]; //INPUT_FILE
		String arg2 = args[1]; //OUTPUT_FILE
		pubmed2geneCount(arg1, arg2);*/
		
		long stopTime = System.currentTimeMillis();
  		long elapsedTime = stopTime - startTime;
  		System.out.println("Execution time in milliseconds: "+elapsedTime);
	}
	
	/**
	 * Method to filter gene2pubmed records (related to human) with abstract 
	 * 
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	
	public static void gene2PubmedHumanWithAbstracts(String arg1, String arg2, String arg3) {
		
		ArrayList<String> pmidlist = new ArrayList<String>();
		
		try {
			FileReader fr0 = new FileReader(arg1);
			BufferedReader br0 = new BufferedReader(fr0);
			while((line = br0.readLine()) != null) {
				pmidlist.add(line.trim());
			}
			
			FileReader fr = new FileReader(arg2);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(pmidlist.contains(arrLine[2].trim())) {
					bw.append(line);
					bw.append("\n");
				}
	
				count1++;
				//if(count1==10) break;
				if(count1%10000==0) System.out.println(count1);
			}
						
			br0.close();
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
	/**
	 * Method to count number of genes mapping to each PubMed
	 * 
	 * @param arg1
	 * @param arg2
	 */
	
	public static void pubmed2geneCount(String arg1, String arg2) {
		
		String pubmedID="", genes="";
		ArrayList<String> geneList = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(pubmedID.isEmpty()) {
					pubmedID=arrLine[2];
					geneList.add(arrLine[1]);
					count++;
				}
				else if(!pubmedID.isEmpty() && pubmedID.equals(arrLine[2])) {
					geneList.add(arrLine[1]);
					count++;
				}
				else if(!pubmedID.isEmpty() && !pubmedID.equals(arrLine[2])) {
					genes="";
					for(String eachgene : geneList) {
						if(genes.isEmpty()) genes = eachgene;
						else genes = genes+"###"+eachgene;
					}
					
					bw.append(pubmedID+"\t"+count+"\t"+genes);
					bw.append("\n");
					
					geneList.clear();
					geneList.add(arrLine[1]);
					count=1;
					pubmedID=arrLine[2];
				}
	
				count1++;
				//if(count1==25) break; 
			}
			genes="";
			for(String eachgene : geneList) {
				if(genes.isEmpty()) genes = eachgene;
				else genes = genes+"###"+eachgene;
			}
			
			bw.append(pubmedID+"\t"+count+"\t"+genes);
			bw.append("\n");
						
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
}
