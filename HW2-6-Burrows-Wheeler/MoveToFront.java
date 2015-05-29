import java.util.LinkedList;


public class MoveToFront {
	
	private static int CODE_LENGTH = 256;
	private static int BIT_LENGTH = 8;
	
	// apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
    	LinkedList<Character> seq = new LinkedList<Character>();
    	for (int i = 0; i < CODE_LENGTH; i++) {
    		seq.add((char) i);
    	}
    	
    	while (!BinaryStdIn.isEmpty()) {
    		char c = BinaryStdIn.readChar();
    		int pos = seq.indexOf(c);
    		
    		seq.remove(pos);
    		seq.addFirst(c);
    		
    		BinaryStdOut.write(pos, BIT_LENGTH);
    	}
    	
    	BinaryStdOut.close();
    	
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	LinkedList<Character> seq = new LinkedList<Character>();
    	for (int i = 0; i < CODE_LENGTH; i++) {
    		seq.add((char) i);
    	}
    	
    	while (!BinaryStdIn.isEmpty()) {
    		int pos = BinaryStdIn.readChar();
    		char c = seq.get(pos);
    		
    		BinaryStdOut.write(c, BIT_LENGTH);
    		
    		seq.remove(pos);
    		seq.addFirst(c);
    	}
    	
    	BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
    	if (args == null || args.length == 0) throw new java.lang.NullPointerException();
    	if (args[0].equals("-")) {
    		encode();
    	} else if (args[0].equals("+")) {
    		decode();
    	}
    }
}
