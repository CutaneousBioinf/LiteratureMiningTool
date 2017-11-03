package edu.uom.med.drugsDictionaryPreProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Certain synonyms in DrugBank includes additional details within a pair of braces and are placed next to the 
 * drug name. However, this is not useful while performing synonym matching between the resources. Therefore, we 
 * developed a program called DrugBankSynonymsList.java to remove the suffix with details within a pair of braces in 
 * DrugBank drug names. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugBankSynonymsList {
	
	/**
	 * Program execution starts here.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- drugbankID_drugsAndSalts_synonyms
		String arg2 = args[1]; //OUTPUT_FILE -- drugBankSynonyms1.txt

		String line="";
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				//remove the content inside the braces
				if(arrLine[1].endsWith(")")&&arrLine[1].matches(".+\\s+\\(\\S+\\)"))  {
					arrLine[1]=arrLine[1].substring(0, arrLine[1].lastIndexOf("("));
				}
				else if(arrLine[1].endsWith("]")&&arrLine[1].matches(".+\\s+\\[\\S+\\]")) { 
					arrLine[1]=arrLine[1].substring(0, arrLine[1].lastIndexOf("["));
				}
				
				if(arrLine.length>2) { //presence of synonyms
					String synonyms="";
					
					if(arrLine[2].contains("###")) {
						String[] drugBankSynonyms = arrLine[2].split("###");
					
						for(String each : drugBankSynonyms) {
							//remove the content inside the braces
							if(each.endsWith(")")&&each.matches(".+\\s+\\(\\S+\\)"))  {
								each=each.substring(0, each.lastIndexOf("("));
							}
							else if(each.endsWith("]")&&each.matches(".+\\s+\\[\\S+\\]")) { 
								each=each.substring(0, each.lastIndexOf("["));
							}
						
							if(synonyms.isEmpty()) synonyms = each;
							else synonyms = synonyms+"###"+each;
						}
					}
					else {
						//remove the content inside the braces
						if(arrLine[2].endsWith(")")&&arrLine[2].matches(".+\\s+\\(\\S+\\)"))  {
							arrLine[2]=arrLine[2].substring(0, arrLine[2].lastIndexOf("("));
						}
						else if(arrLine[2].endsWith("]")&&arrLine[2].matches(".+\\s+\\[\\S+\\]")) { 
							arrLine[2]=arrLine[2].substring(0, arrLine[2].lastIndexOf("["));
						}
					}
					line = arrLine[0]+"###"+arrLine[1]+"###"+arrLine[2];
				}
				else line = arrLine[0]+"###"+arrLine[1];
				
				bw.append(line);
				bw.append("\n");
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
