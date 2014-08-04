package test;

import java.io.FileReader;
import java.io.Reader;
import java.io.StreamTokenizer;

public class StreamTokenizerExample {

	public static void main(String[] args) throws Exception {
		Reader freader = new FileReader("test.reg.txt");
		StreamTokenizer tokenizer = new StreamTokenizer(freader);
		tokenizer.commentChar('*');
		tokenizer.ordinaryChar('$');
		while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			// System.out.println(tokenizer.sval);
		    if(tokenizer.ttype == StreamTokenizer.TT_WORD) {
		        System.out.println(tokenizer.sval);
		    } else if(tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
		        System.out.println(tokenizer.nval);
		    } else if(tokenizer.ttype == StreamTokenizer.TT_EOL) {
		        System.out.println("*************");
		    }
		}
		freader.close();
	}
}
