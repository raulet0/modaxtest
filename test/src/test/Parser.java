package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
	
	static String filename = "test.reg.txt";

	public Parser() {
	}

	private void printLines(List<String[]> lines) {
		for (String[] line : lines)
			System.out.println(Arrays.deepToString(line));
	}
	
	private static void readLinesTrace(int digit, int cursor) {
		if ((digit != '\r') && (digit != '\n')) {
			System.out.println("cursor=" + cursor + "\t" + (char)digit);
		} else {
			System.out.println("cursor=" + cursor); 
		}
	}
	
	public static List<String[]> readLines(final InputStream is) throws IOException {
		byte[] buffer = new byte[1024];
		boolean isreadingstring = false, isreadingcomment = false, isreadingexpression = false,
				pushcursor = false;
		int digit = 0, cursor = 0, nbcr = 0, nblf = 0;
		List<String> tokens = new ArrayList<>();
		List<String[]> lines = new ArrayList<>();
		while ((digit = is.read()) >= 0) {
			// readLinesTrace(digit, cursor);
			buffer[cursor] = (byte)digit;
			pushcursor = false;
			if (isreadingcomment) { // from anywhere until end of line
				if ((digit == '\r') || (digit == '\n')) {
					if (digit == '\r') { nbcr++; } else { nblf++; }
					if ((nbcr == 1) && (nblf == 1)) { // end of line
						if (! tokens.isEmpty()) { 
							lines.add(tokens.toArray(new String[0]));
						}
						tokens.clear();
						nbcr = 0; nblf = 0;
						isreadingcomment = false;
					}
				}
			} else if (isreadingstring) {
				if (digit == '"') { // then this is the end of string
					tokens.add(new String(buffer, 0, cursor + 1)); // " included
					cursor = 0;
					isreadingstring = false;
				} else {
					pushcursor = true;
				}
			} else if (isreadingexpression) {
				if (digit == ']') { // then this is the end of expression
					tokens.add(new String(buffer, 0, cursor + 1)); // ] included
					cursor = 0;
					isreadingexpression = false;
				} else {
					pushcursor = true;
				}
			} else if (digit == '"') {
				if ( ! isreadingstring) { // then this is the beginning of string
					if (cursor > 0) { // first " is also a separator
						tokens.add(new String(buffer, 0, cursor)); // " excluded
					}
					buffer[cursor = 0] = (byte)digit;
				}
				isreadingstring = true;
				pushcursor = true;
			} else if (digit == '[') {
				if ( ! isreadingexpression) { // then this is the beginning of expression
					if (cursor > 0) { // first [ is also a separator
						tokens.add(new String(buffer, 0, cursor)); // [ excluded
					}
					buffer[cursor = 0] = (byte)digit;
				}
				isreadingexpression = true;
				pushcursor = true;
			} else if (((digit == '*') || (digit == '#')) && (cursor == 0)) {
					isreadingcomment = true;
			} else if ((digit == ' ') || (digit == '\t')) {
				if (cursor > 0) { // no first spaces and no double space
					tokens.add(new String(buffer, 0, cursor));
					cursor = 0;
				}
			} else if ((digit == '\r') || (digit == '\n')) {
				if (digit == '\r') { nbcr++; }	else { nblf++; }
				if ((nbcr == 1) && (nblf == 1)) { // end of line
					if (cursor > 0) { // then string not empty!
						tokens.add(new String(buffer, 0, cursor));
					}
					if (! tokens.isEmpty()) { 
						lines.add(tokens.toArray(new String[0]));
					}
					tokens.clear();
					cursor = 0; nbcr = 0; nblf = 0;
					isreadingcomment = false; // CRLF always terminate comments (not strings)
				}
			} else { // not reading comment, not reading string, not special digit
				pushcursor = true;
			}
			if (pushcursor && (cursor + 1 < buffer.length)) { cursor++; } // prevent buffer overflow
		} // while loop
		if (cursor > 0) { 
			tokens.add(new String(buffer, 0, cursor));
		}
		if (! tokens.isEmpty()) {
			lines.add(tokens.toArray(new String[0]));
		}
		return lines;
	}
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir") + File.separator + filename);
		Parser parser = new Parser();
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(filename));
			parser.printLines(readLines(is));
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
