package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Parser {

	public Parser() {
		// TODO Auto-generated constructor stub
	}

	private static void readLine(List<String> tokens) {
		if (tokens.isEmpty()) { System.out.print("EL:"); }
		System.out.println(tokens);
	}

	private static void readRules(final InputStream is) throws IOException {
		byte[] buffer = new byte[10];
		boolean isreadingstring = false, isreadingcomment = false, endofline = false;
		int cursor = 0, digit = 0, nbcr = 0, nblf = 0, nbsp = 0, tokenindex = 0;
		String token = null;
		List<String> tokens = new ArrayList<String>();
		while ((digit = is.read()) >= 0) {
			if (cursor == buffer.length) { cursor--; } // prevent buffer overflow
			buffer[cursor] = (byte)digit;
			cursor++; // now, next position to be filled
			if (isreadingcomment == true) {
				if ((digit == '\r') || (digit == '\n')) {
					if (digit == '\r') { nbcr++; } else { nblf++; }
					endofline = ((nbcr == 1) && (nblf == 1));
					if (endofline) {
						tokens.clear();
						cursor = 0; tokenindex = 0; nbcr = 0; nblf = 0; nbsp = 0;
						isreadingcomment = false;
					}
				}
			} else if (isreadingstring == true) {
				if ((digit == '"') || (digit == ']')) { // then this is the end
					tokens.add(new String(buffer, tokenindex, cursor - tokenindex));
					cursor = 0; tokenindex = 0;
					isreadingstring = false;
				}
			} else if ((digit == '"') || (digit == '[')) {
				if (isreadingstring == false) { // then this is the beginning
					tokenindex = cursor - 1;
				}
				isreadingstring = true;
			} else if ((digit == '*') && (cursor == 1)) {
					isreadingcomment = true;
					// System.out.println("comment");
			} else if ((digit == ' ') || (digit == '\t')) {
				nbsp++;
				if ((cursor == 1) || (nbsp >= 2)) { // first spaces or double space
					cursor--;
				} else {
					if (cursor > tokenindex + 1) { // then string not empty!
						tokens.add(new String(buffer, tokenindex, cursor - tokenindex - 1));
					}
					cursor = 0; tokenindex = 0;
				}
			} else if ((digit == '\r') || (digit == '\n')) {
				if (digit == '\r') { nbcr++; }	else { nblf++; }
				endofline = ((nbcr == 1) && (nblf == 1));
				if (endofline) {
					//System.out.println(cursor + "," + tokenindex);
					if (cursor > tokenindex + 2) { // then string not empty!
						tokens.add(new String(buffer, tokenindex, cursor - tokenindex - 2));
					}
					readLine(tokens);	
					tokens.clear();
					cursor = 0; tokenindex = 0; nbcr = 0; nblf = 0; nbsp = 0; // isolated CR or LF are avoided
					isreadingcomment = false;
				}
			} else {
				// not reading comment, not reading string, not special digit
				nbsp = 0;
			}
		} // while loop
		if (cursor - tokenindex > 0) {
			token = new String(buffer, tokenindex, cursor - tokenindex);
			tokens.add(token);
		}
		readLine(tokens);
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dir = System.getProperty("user.dir") + File.separator;
		System.out.println(dir);
		try {
			//InputStream is = new BufferedInputStream(new FileInputStream("C:/User/workspace_java/Modax/bases/test.reg.txt"));
			InputStream is = new BufferedInputStream(new FileInputStream("test.reg.txt"));
			readRules(is);
			is.close();
		} catch (Exception e) {
			// System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
