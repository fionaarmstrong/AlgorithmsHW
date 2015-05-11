import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeSet;



public class BoggleSolver {

	private class Node {
		boolean isWord;
		char value;
		Node left;
		Node right;
		Node child;

		Node(char v) {
			this.value = v;
		}
	}

	// A 3-d array representing first 3 character,
	// The Node value refers to the last character
	private Node[][][] three;

	// Initializes the data structure using the given array of strings as the dictionary.
	// (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		three = new Node[26][26][26];

		for (String s: dictionary) {
			addWord(s);
		}
	}

	private void addWord(String s) {
		if (s == null || s.length() < 3) return;
		s = s.toUpperCase();

		int idx0 = s.charAt(0) - 'A';
		int idx1 = s.charAt(1) - 'A';
		int idx2 = s.charAt(2) - 'A';

		if (three[idx0] == null)
			three[idx0] = new Node[26][26];
		if (three[idx0][idx1] == null)
			three[idx0][idx1] = new Node[26];
		if (three[idx0][idx1][idx2] == null)
			three[idx0][idx1][idx2] = new Node(s.charAt(2));

		int i = 2;
		Node n = three[idx0][idx1][idx2];

		while (i < s.length()-1) {
			n = getNextChar(n, s.charAt(++i));
		}

		n.isWord = true;
	}

	/*
	 * Given parent node p, locate the next character in the sub-tree
	 * Create if not exist
	 */
	private Node getNextChar(Node p, char c) {
		if (p.child == null) {
			p.child = new Node(c);
			return p.child;
		}

		Node n = p.child;
		while (true) {
			if (n.value == c) {
				return n;
			} else if (n.value > c) {
				if (n.right == null) {
					n.right = new Node(c);
					return n.right;
				} else {
					n = n.right;
				}
			} else {
				if (n.left == null) {
					n.left = new Node(c);
					return n.left;
				} else {
					n = n.left;
				}
			}
		}
	}

	private void visitNeighbor(BoggleBoard board, int ii, int jj
			, LinkedList<Integer> stacki, LinkedList<Integer> stackj
			, LinkedList<String> stackS, LinkedList<ArrayList<Integer>> stackPathi
			, LinkedList<ArrayList<Integer>> stackPathj, ArrayList<Integer> iiPath
			, ArrayList<Integer> jjPath, String s, TreeSet<String> res) {


		char c = board.getLetter(ii, jj);
		String ss = s;
		if (c == 'Q')
			ss = ss + "QU";
		else
			ss = ss + c;

		if (!isExistingPrefix(ss)) return;

		if (scoreOf(ss) > 0) {
			//			System.out.println("::" + ss);
			res.add(ss);
		}
		stacki.add(ii);
		stackj.add(jj);
		stackS.add(ss);

		ArrayList<Integer> rowPath = new ArrayList<Integer>();
		rowPath.addAll(iiPath);
		rowPath.add(ii);
		stackPathi.add(rowPath);

		ArrayList<Integer> colPath = new ArrayList<Integer>();
		colPath.addAll(jjPath);
		colPath.add(jj);
		stackPathj.add(colPath);
	}

	private Collection<String> dfs(BoggleBoard board, int i, int j) {
		TreeSet<String> res = new TreeSet<String>();

		LinkedList<Integer> stacki = new LinkedList<Integer>();
		LinkedList<Integer> stackj = new LinkedList<Integer>();
		LinkedList<String> stackS = new LinkedList<String>();
		LinkedList<ArrayList<Integer>> stackPathi = new LinkedList<ArrayList<Integer>>();
		LinkedList<ArrayList<Integer>> stackPathj = new LinkedList<ArrayList<Integer>>();
		stacki.add(i);
		stackj.add(j);
		char letter = board.getLetter(i, j);
		if (letter == 'Q')
			stackS.add("QU");
		else stackS.add("" + letter);


		ArrayList<Integer> path0 = new ArrayList<Integer>();
		path0.add(i);
		stackPathi.add(path0);
		ArrayList<Integer> path1 = new ArrayList<Integer>();
		path1.add(j);
		stackPathj.add(path1);

		while (!stacki.isEmpty()) {
			int ii = stacki.pollLast();
			int jj = stackj.pollLast();
			String s = stackS.pollLast();
			ArrayList<Integer> iiPath = stackPathi.pollLast();
			ArrayList<Integer> jjPath = stackPathj.pollLast();
			boolean[][] marked = new boolean[3][3];
			marked[0] = new boolean[]{true, true, true};
			marked[1] = new boolean[]{true, true, true};
			marked[2] = new boolean[]{true, true, true};

			if (ii == 0) {
				marked[0][0] = false;
				marked[0][1] = false;
				marked[0][2] = false;
			} 
			if (ii == board.rows()-1) {
				marked[2][0] = false;
				marked[2][1] = false;
				marked[2][2] = false;
			}
			if (jj == 0) {
				marked[0][0] = false;
				marked[1][0] = false;
				marked[2][0] = false;
			} 
			if (jj == board.cols()-1) {
				marked[0][2] = false;
				marked[1][2] = false;
				marked[2][2] = false;
			}

//			System.out.println("Edge check: ii=" + ii + " jj=" + jj + " row=" + board.rows() + " col=" + board.cols());
//			for (int iii = 0; iii < 3; iii++) {
//				for (int jjj = 0; jjj < 3; jjj++) {
//					System.out.print(marked[iii][jjj] + " ");
//				}
//				System.out.println();
//			}

			for (int k = 0; k < iiPath.size(); k++) {
				if (iiPath.get(k) == ii-1) {
					if (jjPath.get(k) == jj-1)
						marked[0][0] = false;
					else if (jjPath.get(k) == jj)
						marked[0][1] = false;
					else if (jjPath.get(k) == jj+1)
						marked[0][2] = false;		
				} else if (iiPath.get(k) == ii) {
					if (jjPath.get(k) == jj-1)
						marked[1][0] = false;
					else if (jjPath.get(k) == jj+1)
						marked[1][2] = false;
				} else if (iiPath.get(k) == ii+1) {
					if (jjPath.get(k) == jj-1)
						marked[2][0] = false;
					else if (jjPath.get(k) == jj)
						marked[2][1] = false;
					else if (jjPath.get(k) == jj+1)
						marked[2][2] = false;
				}
			}

			for (int iii = 0; iii < 3; iii++) {
				for (int jjj = 0; jjj < 3; jjj++) {
					if (iii == 1 && jjj == 1) continue;
					else if (marked[iii][jjj]) {
						visitNeighbor(board, ii + iii - 1, jj + jjj - 1, stacki, stackj, stackS
								, stackPathi, stackPathj, iiPath, jjPath, s, res);
					}
				}
			}
		}

		return res;
	}

	// Returns the set of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		if (board == null || board.rows() == 0 || board.cols() == 0)
			return null;

		TreeSet<String> res = new TreeSet<String>();
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				res.addAll(dfs(board, i, j));
			}
		}

		return res;
	}

	private boolean isExistingPrefix(String s) {
		if (s.length() == 1) {
			return three[s.charAt(0) - 'A'] != null;
		} else if (s.length() == 2) {
			return three[s.charAt(0) - 'A'][s.charAt(1) - 'A'] != null;
		} 

		Node n = three[s.charAt(0) - 'A'][s.charAt(1) - 'A'][s.charAt(2) - 'A'];
		if (n == null) return false;

		int i = 2;
		while (i < s.length() - 1) {
			n = isNextChar(n, s.charAt(++i));
			if (n == null) return false;
		}
		return true;
	}

	// Returns the score of the given word if it is in the dictionary, zero otherwise.
	// (You can assume the word contains only the uppercase letters A through Z.)
	public int scoreOf(String word) {
		if (word == null || word.length() < 3) return 0;

		Node n = three[word.charAt(0) - 'A'][word.charAt(1) - 'A'][word.charAt(2) - 'A'];
		if (n == null) return 0;
		int i = 2;

		while (i < word.length() - 1) {
			n = isNextChar(n, word.charAt(++i));
			if (n == null) return 0;
		}

		if (!n.isWord) return 0;

		// If code reaches here, the word is in the dictionary
		if (word.length() <= 4) return 1;
		else if (word.length() <= 5) return 2;
		else if (word.length() <= 6) return 3;
		else if (word.length() <= 7) return 5;
		else return 11;
	}

	/*
	 * Given parent node p, locate the next character in the sub-tree
	 * Return null if not exist
	 */
	private Node isNextChar(Node p, char c) {
		Node n = p.child;
		while (true) {
			if (n == null)
				return null;

			if (n.value == c) {
				return n;
			} else if (n.value > c) {
				n = n.right;
			} else {
				n = n.left;
			}
		}
	}



	public static void main(String[] args) {	
		// all words in shakespeare
		In in = new In("boggle/dictionary-yawl.txt");
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);

		//        System.out.println("DET::" + solver.scoreOf("DET"));
		//        System.out.println("DETA::" + solver.scoreOf("DETA"));

		BoggleBoard board = new BoggleBoard("boggle/board-q.txt");
//		BoggleBoard board = new BoggleBoard(1, 10);
		StdOut.println(board);
		StdOut.println();
		int score = 0;
		for (String word : solver.getAllValidWords(board))
			//        StdOut.println("Checking:" + board.getLetter(3, 1));
			//        for (String word : solver.dfs(board, 3, 1))
		{
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);

	}

}
