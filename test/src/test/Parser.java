package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class Parser {

	public Parser() {
		// TODO Auto-generated constructor stub
	}

	private static void readLine(String[] tokens) {
		
	}

	private static void readRules(final InputStream is) throws IOException {
		byte[] buf = new byte[1024];
		boolean begin_quote = false;
		boolean comment = false;
		String token = null;
		int nbpar = 0;
		int nread = 0, ch = 0, nbcr = 0, nblf = 0, index_nl = 0, pos = 0;
		boolean end_of_line = false, end_of_head = false;
		// to prevent buffer overflow, another strategy could be to continue to read
		// the stream without writing anything (stop to increment nread ?)...
		// read return -1 if end of stream (end of file).
		// NOTE : if socket closed then read return -1 immediately.
		while ((nread < buf.length) && ((ch = is.read()) >= 0)) { // scan headers
			buf[nread] = (byte)ch;
			nread++; // now, next position to be filled
			switch(ch) {
			case '\r': 
				nbcr++;
				break;
			case '\n': 
				nblf++;
				break;
			case ' ':
				token = new String(buf, index_nl, nread - index_nl - 2);
				break;
			case '"':
				begin_quote = !begin_quote;
				break;
			case '[':
				nbpar++;
				break;
			case ']':
				nbpar--;
				break;
			case '*':
				comment = true; // in first position ???
				break;
			default:
				break;
			}
			end_of_line = ((nbcr == 1) && (nblf == 1));
			end_of_head = ((nbcr == 2) && (nblf == 2));
			if (end_of_line == true) {
			}
			nbcr = 0; // isolated CR ou LF are avoided
			nblf = 0;

			/*
			if ((ch == '\r') || (ch == '\n')) {
				if (ch == '\r') {
					nbcr++;
				}
				else {
					nblf++;
				}
				end_of_line = ((nbcr == 1) && (nblf == 1));
				end_of_head = ((nbcr == 2) && (nblf == 2));
				if (end_of_line == true) {
					lineread = new String(buf, index_nl, nread - index_nl - 2); // without CRLF
					index_nl = nread; // beginning of a new line for a new field...
				}
				else if (end_of_head == true) { break; } // here, nread - index_nl == 2
			}
			else {
				nbcr = 0; // isolated CR ou LF are avoided
				nblf = 0;
			}
			 */
		} // while loop
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
			System.err.println("Error: " + e.getMessage());
		}

	}

}
