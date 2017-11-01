package edu.uom.med.cgapPathways;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program to get list of genes and functions (GO annotations) from Biocarta (or CGAP)
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class CGAPgenes_and_functions {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="", geneSymbol="", pathway="", output="";
		int count=0;
		
		ArrayList<String> data = new ArrayList<String>();
		
		String arg1 = args[0]; //c5.all.v6.0.symbols.gmt (refer Documentation/Drug_gene_pathway.docx)
		String arg2 = args[1]; //OUTPUT_FILE
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			while((line=br.readLine()) != null) {
				data.add(line);
			}
			
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			ArrayList<String> eachdata = new ArrayList<String>();
			bw.append("Gene_symbol\tFunctions_list");
			bw.append("\t");
			for(String eachline : data) {
				if(eachline.startsWith(">>")) {
					if(eachdata.isEmpty()) continue;
					
					if(eachdata.size()==1) {
						geneSymbol = eachdata.get(0);
						geneSymbol = geneSymbol.substring(geneSymbol.indexOf(":")+2);
						pathway = "none";
					}
					else {
						for(String each : eachdata) {
							if(each.startsWith("SYMBOL:")) geneSymbol = each.substring(each.indexOf(":")+2);
							else if(each.startsWith("GO:")) {
								String eachpathway = each.substring(each.indexOf(":")+2);
								eachpathway = eachpathway.substring(eachpathway.indexOf("|")+1);
								
								if(pathway.isEmpty()) pathway = eachpathway;
								else pathway = pathway + "|" + eachpathway;
							}
						}
					}
					
					if(!geneSymbol.isEmpty()) {
						output = geneSymbol + "\t" + pathway;
						bw.append(output);
						bw.append("\n");
					}
					
					eachdata.clear();
					output="";
					geneSymbol="";
					pathway="";
					
					count++;
					if(count==10) break;
				}
				else {
					if(eachline.startsWith("SYMBOL:") || eachline.startsWith("GO:")) eachdata.add(eachline);
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
	
}
