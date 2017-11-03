package edu.uom.med.testMapping;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Program maps drug mentions from PharmGKB to DrugBank. The drug names are normalized
 * prior to mapping. The output of the program is the drugs present in both the resources.
 * This knowledge is is useful to map UMLS to DrugBank while creating the chemicals/drugs
 * lexicon.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class TestPharmGKB2DrugBankNormalized {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		
		String arg1 = args[0]; //INPUT_FILE -- drugBankSynonyms_normalized
		String arg2 = args[1]; //INPUT_FILE -- pharmgkbSynonyms_normalized
		String arg3 = args[2]; //OUTPUT_FILE -- PharmGKB2DrugBank_normalized
		String arg4 = args[3]; //OUTPUT_FILE -- PharmGKB2DrugBank_not_mapped_normalized

		String line="";
		int count=0, count1=0;
		ArrayList<String> drugBankDrugs = new ArrayList<String>();
		
		try {
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
		    	String[] arrLine = line.split("###");
		    	for(String each : arrLine) {
		    		drugBankDrugs.add(arrLine[1].trim());
		    	}
		    }
			
			FileInputStream fis = new FileInputStream(arg2);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		    FileOutputStream fos1 = new FileOutputStream(arg4);
		    OutputStreamWriter osr1 = new OutputStreamWriter(fos1, "UTF-8");
		    BufferedWriter bw1 = new BufferedWriter(osr1);
		    while((line = br.readLine()) != null) {
		    	boolean flag = false;
				String[] arrLine = line.split("###");
				
				for(int i=1; i<arrLine.length; i++) {
					//drugs mapping to UMLS
					if(drugBankDrugs.contains(arrLine[i].trim())) {
						bw.append(arrLine[0]+"\t"+arrLine[i].trim());
						bw.append("\n");
						flag=true;
						count1++;
						break;
					}
					else if(drugBankDrugs.contains(arrLine[i].trim().toLowerCase())) {
						bw.append(arrLine[0]+"\t"+arrLine[i].trim());
						bw.append("\n");
						flag=true;
						count1++;
						break;
					}
				}
				
				//drugs not mapping to UMLS
				if(!flag) {
					bw1.append(line);
					bw1.append("\n");
				}
				
				count++;
				//if(count==10) break;
			}
		    System.out.println(count1);
			br.close();
			bw.close();
			bw1.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
}
