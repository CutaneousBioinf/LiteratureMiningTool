package edu.uom.med.conceptsDictionaryGenerator;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/** 
 * The MRSTYCuiTuiGrouper program implements an application 
 * to assign respective TUI to each CUI
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */

public class MRSTYCuiTuiGrouper {
	
	/**
	 * Method assigns respective TUI to each CUI
	 * 
	 * @throws IOException
	 */
	
	public void cuiTuiGrouper() throws IOException {
		
		System.out.println("Grouping CUI and TUI for each concept or its synonyms....");
		
		BufferedReader in = new BufferedReader(new FileReader("resources/secondaryDictionaries/mrsty.cuitui2016AA.txt"));
		PrintWriter pwr = new PrintWriter("resources/secondaryDictionaries/mrsty.cuituiGroup2016AA.txt");
		
		ArrayList<String> cuiTuiGroup = new ArrayList<String>();
		String cuilist="";
		
		while(in.ready()){
			String line = in.readLine();
			String[] splits=line.split("\t");
			String cui=splits[0].substring(0, splits[0].indexOf("_"));
			if(cuilist.isEmpty()) { 
				cuilist=splits[0]; //cui_tui
			}
			else { 
				if(cuilist.startsWith(cui)) { 
					cuilist=cuilist.concat(";").concat(splits[0]); 
				}
				else {
					cuiTuiGroup.add(cuilist);
					cuilist="";
					cuilist=splits[0];
				}
			}
		}
		in.close();
		
		for(String eachCuiTui : cuiTuiGroup) {
			pwr.println(eachCuiTui);
		}
		pwr.close();
		
		System.out.println("Total number of records: "+cuiTuiGroup.size());
	}
	
}
