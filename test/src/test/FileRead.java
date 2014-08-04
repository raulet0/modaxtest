package test;

import java.io.*;
import java.util.Arrays;
class FileRead 
{
	public static void main(String args[])
	{
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("test.reg.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				String[] sentence = strLine.trim().split("\\s");
				System.out.println (Arrays.deepToString(sentence) + sentence.length);
			}
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}