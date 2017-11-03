package pharmGKBFileProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Program collects the drugs from PharmGKB that were not mapped to UMLS Metathesaurus
 * and DrugBank in Loop1 and Loop2 execution. This is used as input for Loop3 exectution. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PharmGKBDrugs4Loop3 {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- pharmgkbSynonyms
		String arg2 = args[1]; //INPUT_FILE -- PharmGKBID4Loop3
		String arg3 = args[2]; //OUTPUT_FILE -- PharmGKBLoop3_input

		String line="";
		
		ArrayList<String> records = new ArrayList<String>();
		
		try {
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
				records.add(line.trim());
			}
		         
		    FileInputStream fis = new FileInputStream(arg2);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				for(String each : records) {
					if(each.startsWith(line+"###")) {
						bw.append(each);
						bw.append("\n");
					}
				}
				//System.out.println(line);
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
