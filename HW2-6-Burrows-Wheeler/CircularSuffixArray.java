import java.util.HashMap;


public class CircularSuffixArray {
	private String s;
	private int[] index;
	private int ENCODING = 256;
	
	// circular suffix array of s
	public CircularSuffixArray(String s) {
		if (s == null) throw new java.lang.NullPointerException();
		this.s = s;
		index = new int[s.length()];
		
		for (int i = 0; i < index.length; i++) {
			index[i] = i;
		}
		
		sortRange(index, 0, s.length()-1, 0);
		
//		for (int i: index) 
//			System.out.println(i);
	}
	
	private void sortRange(int[] index, int start, int end, int level) {
		int[] charCount = new int[ENCODING];
		int[] sorted = new int[end - start + 1];
		HashMap<Integer, Integer[]> recursion = new HashMap<Integer, Integer[]>();
		
		for (int i = start; i <= end; i++) {
			charCount[(int) charAt(index[i], level)]++;
		}
		
//		for (int i = 0; i < charCount.length; i++) {
//			if (charCount[i] != 0)
//				System.out.println((char)i + " " + charCount[i]);
//		}
//		System.out.println();
		
		int sum = 0;
		for (int i = 0; i < charCount.length; i++) {
			if (charCount[i] != 0) {
				int j = charCount[i];
				charCount[i] = sum;
				if (j > 1) {
					recursion.put(i, new Integer[]{sum, j});
				}
				sum += j;
			}
		}
		
//		for (int i : recursion.keySet()) {
//			System.out.println((char) i + " " + recursion.get(i)[0] + " " + recursion.get(i)[1]);
//		}
		
		for (int i = 0; i < sorted.length; i++) {
			char c = charAt(index[start + i], level);
			sorted[charCount[(int)c]] = index[start + i];
			charCount[(int)c]++;
			
		}
		
		for (int i = 0; i < sorted.length; i++) {
			index[start+i] = sorted[i];
		}
			
		// Recursion of the sub arrays
		for (int i: recursion.keySet()) {
//			System.out.println((char) i + " Level:" + level +" " + (start+recursion.get(i)[0]) + " " + (start + recursion.get(i)[0] + recursion.get(i)[1] - 1));
			sortRange(index, start + recursion.get(i)[0], start + recursion.get(i)[0] + recursion.get(i)[1] - 1, level+1);
		}
	}
	
	// Return ith original Suffixes's jth char
	private char charAt(int i, int j) {
		return s.charAt((i+j)%s.length());
	}
	
	// length of s
	public int length() {
		return s.length();
	}
	
	// returns index of ith sorted suffix
	public int index(int i) {
		if (i < 0 || i >= s.length()) throw new java.lang.IndexOutOfBoundsException();
		return index[i];
	}
	
	// unit testing of the methods (optional)
	public static void main(String[] args) {
		CircularSuffixArray obj = new CircularSuffixArray("ABRACADABRA!");
	}

}
