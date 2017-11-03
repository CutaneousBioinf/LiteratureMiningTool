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
 * Program maps drug mentions between resources: DrugBank to UMLS Metathesaurus, 
 * PharmGKB to DrugBank and PharmGKB to UMLS Metathesaurus. The output of the
 * program is the drugs present in both the resources. This knowledge is
 * is useful to map UMLS to DrugBank while creating the chemicals/drugs lexicon.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class TestMapping {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE1 
		String arg2 = args[1]; //INPUT_FILE2 
		String arg3 = args[2]; //OUTPUT_FILE1 
		String arg4 = args[3]; //OUTPUT_FILE2 
		String arg5 = args[4]; //OUTPUT_FILE3 
		
		String line="";
		int count=0, count1=0, count2=0;
		ArrayList<String> umlsDrugs = new ArrayList<String>();
		ArrayList<String> umlsDrugsNormalized = new ArrayList<String>();
		
		try {
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
		    	String[] arrLine = line.split("\t");
		    	umlsDrugs.add(arrLine[1].trim().toLowerCase());
		    }
		    
		    FileInputStream fis1 = new FileInputStream(arg2);
			InputStreamReader isr1 = new InputStreamReader(fis1,"UTF-8");
		    BufferedReader br1 = new BufferedReader(isr1);
		    while((line = br1.readLine()) != null) {
		    	String[] arrLine = line.split("\t");
		    	umlsDrugsNormalized.add(arrLine[1].trim().toLowerCase());
		    }
			
			FileInputStream fis = new FileInputStream(arg3);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg4);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		    FileOutputStream fos1 = new FileOutputStream(arg5);
		    OutputStreamWriter osr1 = new OutputStreamWriter(fos1, "UTF-8");
		    BufferedWriter bw1 = new BufferedWriter(osr1);
		    while((line = br.readLine()) != null) {
		    	boolean flag = false;
				String[] arrLine = line.split("###");
				
				for(int i=1; i<arrLine.length; i++) {
					//remove the content inside the braces
					if(arrLine[i].endsWith(")")&&arrLine[i].matches(".+\\s+\\(\\S+\\)"))  {
						arrLine[i]=arrLine[i].substring(0, arrLine[i].lastIndexOf("("));
					}
					else if(arrLine[i].endsWith("]")&&arrLine[i].matches(".+\\s+\\[\\S+\\]")) { 
						arrLine[i]=arrLine[i].substring(0, arrLine[i].lastIndexOf("["));
					}
					
					//drugs mapping to UMLS
					if(umlsDrugs.contains(arrLine[i].trim().toLowerCase())) { //exact match
						bw.append(arrLine[0]+"\t"+arrLine[i].trim());
						bw.append("\n");
						flag=true;
						count1++;
						break;
					}
					else { //normalized match
						String temp = arrLine[i];
						if(temp.contains("-")) {
							temp = temp.replaceAll("-", " ");
							temp = temp.replaceAll(" ", "");
							if(umlsDrugsNormalized.contains(temp.trim().toLowerCase())) { 
								bw.append(arrLine[0]+"\t"+arrLine[i].trim());
								bw.append("\n");
								flag=true;
								count1++;
								break;
							}
						}
					}
				}
				
				//drugs not mapping to UMLS
				if(!flag) {
					bw1.append(line);
					bw1.append("\n");
					count2++;
				}
				
				count++;
				//if(count==10) break;
			}
		    System.out.println("Mapped: "+count1);
		    System.out.println("Not Mapped: "+count2);
		    br0.close();
			br.close();
			br1.close();
			bw.close();
			bw1.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time in milliseconds: " + elapsedTime);
	}

}
