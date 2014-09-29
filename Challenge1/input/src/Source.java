/*
// Purpose		:	To implement twitter sentiment analysis for Real time big data challenge 1
//Owner			:	Group 4 Members
//Team Members	:	Srikar Reddy Mallareddygari
//					Pavan Kumar Bollaram

*/
// Import for Buffered Writer Library 

import java.io.BufferedReader;

// Import for java.io.* Library 

import java.io.*;

// Import for CSV Library 

import com.csvreader.*;


public class Source {   


	public static void main(String agrs[]) throws IOException
		{                           
		
				String fileName="/home/cloudera/Desktop/filename1.txt";
    		
    			FileReader input = new FileReader(fileName);
    			BufferedReader br = new BufferedReader(input);
    			String line;
    			while ((line = br.readLine()) != null)   {
			
						
			String[] shorttext = line.split("\""+"text"+"\""+"\\:"+"\"");
			
			for (int j = 1;j<=shorttext.length-1;j++)
			{
			String[] text2 = shorttext[j].split("\""+"\\,");
			System.out.println(text2[0]);


			String csv = "/home/cloudera/Desktop/tweetoutput.csv";
			try {
				// use FileWriter constructor that specifies open for appending
				CsvWriter Fwriter = new CsvWriter(new FileWriter(csv, true), ',');
				
 
				Fwriter.write(text2[0]);
				Fwriter.write("o");
				Fwriter.endRecord();
				Fwriter.close();
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			}
			
			
			}

	}
}