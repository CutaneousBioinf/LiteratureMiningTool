package edu.uom.med.cgapPathways;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program processes the pathway resource file from CGAP (or Biocarta)
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PathwaysAndFunctionsFromCGAP {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="", output="", geneSymbol="", pathway="";
		int count=0;
		
		String arg1 = args[0]; //Hs_GeneData.dat (refer Documentation/Drug_gene_pathway.docx)
		String arg2 = args[1]; //OUTPUT_FILE_BIOCARTA (refer Documentation/Drug_gene_pathway.docx)
		
		ArrayList<String> data = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			while((line=br.readLine()) != null) {
				data.add(line);
			}
			
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			ArrayList<String> eachdata = new ArrayList<String>();
			eachdata.add("Gene_symbol\tPathways");
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
							else if(each.startsWith("BIOCARTA:") || each.startsWith("KEGG:") || each.startsWith("GO:")) {
								String eachpathway = each.substring(each.indexOf(":")+2);
								eachpathway = eachpathway.substring(eachpathway.indexOf("|")+1);
								
								if(pathway.isEmpty()) pathway = eachpathway;
								else pathway = pathway + "|" + eachpathway;
							}
						}
					}
					
					output = geneSymbol + "\t" + pathway;
					bw.append(output);
					bw.append("\n");
					
					eachdata.clear();
					output="";
					geneSymbol="";
					pathway="";
					
					count++;
					if(count==10) break;
				}
				else {
					if(eachline.startsWith("SYMBOL:") || eachline.startsWith("BIOCARTA:") || 
							eachline.startsWith("KEGG:") || eachline.startsWith("GO:")) eachdata.add(eachline);
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
