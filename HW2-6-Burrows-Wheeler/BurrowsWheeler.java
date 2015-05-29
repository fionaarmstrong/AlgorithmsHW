import java.util.Arrays;


public class BurrowsWheeler {
	private static int BIT_LENGTH = 8;
	
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
	public static void encode() {

		String s = BinaryStdIn.readString();
		//String s = "ABRACADABRA!";
		//System.out.println(s);
		CircularSuffixArray csa = new CircularSuffixArray(s);

		for (int i = 0; i < csa.length(); i++) {
			if (csa.index(i) == 0) {
				BinaryStdOut.write(i);
				break;
			}
		}

		for (int i = 0; i < csa.length(); i++) {
			int idx = (csa.index(i) + s.length() - 1) % s.length();
			BinaryStdOut.write(s.charAt(idx), BIT_LENGTH);
		}
		
		BinaryStdOut.close();

    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
    	int first = BinaryStdIn.readInt();
    	char[] t = BinaryStdIn.readString().toCharArray();
    	
//    	int first = 3;
//    	char[] t = "ARD!RCAAAABB".toCharArray();
    	
    	char[] sorted = t.clone();
    	Arrays.sort(sorted);
    	
    	int[] next = new int[t.length];
    	
    	for (int i = 0; i < sorted.length; i++) {
    		int j = 0;
    		if (i != 0 && sorted[i-1] == sorted[i]) {
    			j = next[i-1] + 1;
    		}
    		
    		while (j < next.length) {
    			if (t[j] == sorted[i]) {
    				next[i] = j;
    				break;
    			}
    			j++;
    		}
    		
    	}
    	
    	for (int i = 0; i < next.length; i++) {
    		BinaryStdOut.write(sorted[first]);
    		first = next[first];
    	}
    	BinaryStdOut.close();
//    	for (int i: next)
//    		System.out.print(i+" ");
//    	for (char c: t)
//    		System.out.print(c);
//    	System.out.println();
//    	for (char c: sorted)
//    		System.out.print(c);
    	
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
//    	BurrowsWheeler.decode();
    	if (args == null || args.length == 0) throw new java.lang.NullPointerException();
    	if (args[0].equals("-")) {
    		encode();
    	} else if (args[0].equals("+")) {
    		decode();
    	}
    }
}
