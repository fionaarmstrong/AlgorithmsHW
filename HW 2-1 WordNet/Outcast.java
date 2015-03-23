import java.util.*;


public class Outcast {
	private WordNet wordNet;
	
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		this.wordNet = wordnet;
		
	}
	
	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		int[][] disMatrix = new int[nouns.length][nouns.length];
		for (int x = 0; x < nouns.length; x++)
			for (int y = x; y < nouns.length; y++) {
				if (x == y) 
					disMatrix[x][x] = 0;
				else {
					int dis = wordNet.distance(nouns[x], nouns[y]);
					disMatrix[x][y] = dis;
					disMatrix[y][x] = dis;
				}
			}
		int maxSum = -1;
		int maxId = -1;
		for (int x = 0; x < nouns.length; x++) {
			int rowSum = 0;
			for (int y = 0; y < nouns.length; y++) {
				rowSum += disMatrix[x][y]; 
			}
			if (rowSum > maxSum) {
				maxSum = rowSum;
				maxId = x;
			}
		}
		return nouns[maxId];
	}
	
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			ArrayList<String> list = new ArrayList<String>();
			In in = new In(args[t]);
			while (in.hasNextLine()) {
				list.add(in.readLine());
			}
			String[] nouns = new String[list.size()];
			nouns = (String[]) list.toArray(nouns);
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}

}
