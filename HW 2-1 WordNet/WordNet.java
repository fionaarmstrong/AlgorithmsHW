import java.util.*;

public class WordNet {
	private HashMap<Integer, String[]> idMap;
	private HashMap<String, HashSet<Integer>> wordMap;
	private Digraph dig;
	private SAP sap;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		// Read and parse synsets
		In inSynsets = new In(synsets);
		idMap = new HashMap<Integer, String[]>();
		wordMap = new HashMap<String, HashSet<Integer>>();
		while (inSynsets.hasNextLine()) {
			String[] segments = inSynsets.readLine().split(",");
			String[] words = segments[1].split(" ");
			int id = Integer.parseInt(segments[0]);
			idMap.put(id, words);
			for (String word: words) {
				if (!wordMap.containsKey(word)) {
					HashSet<Integer> list = new HashSet<Integer>();
					list.add(id);
					wordMap.put(word, list);
				} else {
					wordMap.get(word).add(id);
				}
			}
		}
		
		// create digraph using hypernyms file
		dig = new Digraph(idMap.size());
		In inHypernyms = new In(hypernyms);
		while (inHypernyms.hasNextLine()) {
			String[] segments = inHypernyms.readLine().split(",");
			for (int i = 1; i < segments.length; i++)
				dig.addEdge(Integer.parseInt(segments[0]), Integer.parseInt(segments[i]));
		}
		
		// Check if is rooted DAG
		if (!isRootedDAG(dig))
			throw new java.lang.IllegalArgumentException("Digraph is not rooted DAG.");
		
		//System.out.println(dig.toString());
	}
	
	// rooted DAG: it is acylic and has one
	// vertex that is an ancestor of every other vertex.
	private static boolean isRootedDAG(Digraph d) {
		//throws exception if the input is not a rooted DAG
		Digraph revDig = d.reverse();
		int root = -1;
		for (int i = 0; i < d.V(); i++) {
			if (!d.adj(i).iterator().hasNext()) {
				if (root == -1)
					root = i;
				// there can't be more than one root
				else return false;
			}
		}
		// There's no root, has to be a loop
		if (root == -1) return false;
		
		//BFS starting from root
		java.util.Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(root);
		int[] check = new int[d.V()];
		check[root] = root;
		while (!queue.isEmpty()) {
			int current = queue.remove();
			for (int i: revDig.adj(current)) {
				check[i] = check[root];
				queue.add(i);
			}
		}
		
		// Check if every one in the array is pointed to root
		for (int i: check) {
			if (i != root)
				return false;
		}

		return true;
	}
	
	// the set of nouns (no duplicates), returned as an Iterable
	// O(nlogn)
	public Iterable<String> nouns() {
		HashSet<String> resultSet = new HashSet<String>();
		for (String[] ss: idMap.values()) {
			for (String s: ss)
				resultSet.add(s);
		}
		return resultSet;
	}

	// is the word a WordNet noun?
	// O(logn)
	public boolean isNoun(String word) {
		return wordMap.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		//throws exception unless both noun are WordNet nouns
		if (!isNoun(nounA) || !isNoun(nounB)) 
				throw new java.lang.IllegalArgumentException("Input is not a wordnet noun");
		if (nounA.equalsIgnoreCase(nounB)) return 0;
		
		if (sap == null)	
			sap = new SAP(dig);
		
		HashSet<Integer> idA = wordMap.get(nounA);
		HashSet<Integer> idB = wordMap.get(nounB);
		
		return sap.length(idA, idB);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		//throws exception unless both noun are WordNet nouns
		if (!isNoun(nounA) || !isNoun(nounB)) 
				throw new java.lang.IllegalArgumentException();
		if (nounA.equalsIgnoreCase(nounB)) return nounA;
		
		if (sap == null)
			sap = new SAP(dig);
		
		HashSet<Integer> idA = wordMap.get(nounA);
		HashSet<Integer> idB = wordMap.get(nounB);
		
		String[] ancestors = idMap.get(sap.ancestor(idA, idB));
		StringBuffer sb = new StringBuffer();
		for (String s: ancestors) {
			sb.append(s + " ");
		}
		return sb.toString();
	}

	// for unit testing of this class
	public static void main(String[] args) {
		WordNet wordNet = new WordNet("data/synsets.txt", "data/hypernyms.txt");
		System.out.println("ss isNoun " + wordNet.isNoun("ss"));
		String str1 = "Black_Death";
		String str2 = "black_marlin";
		
		System.out.println("Distance between " + str1 + " and " + str2 + ":" + wordNet.distance(str1, str2) 
				+ " ancestor:" + wordNet.sap(str1, str2));
	}
	
	
}
