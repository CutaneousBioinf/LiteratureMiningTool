package drugBankFileProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program collects the drugs from DrugBank that were not mapped to UMLS Metathesaurus
 * in Loop1 execution. This is used as input for Loop2 exectution. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugBankID4Loop2 {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- UMLS2DrugBankID_list
		String arg2 = args[1]; //INPUT_FILE -- drugbankID_TUI_drugsAndSalts_synonyms_grouped
		String arg3 = args[2]; //OUTPUT_FILE -- DrugBankID4Loop2

		String line="";
		ArrayList<String> id = new ArrayList<String>();
		
		try {
			FileReader fr0 = new FileReader(arg1);
			BufferedReader br0 = new BufferedReader(fr0);
			while((line = br0.readLine()) != null) {
				id.add(line.trim());
			}
			
			FileReader fr = new FileReader(arg2);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("###");
				if(!id.contains(arrLine[0].trim())) {
					bw.append(line);
					bw.append("\n");
				}	
			}
			br0.close();
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
