public class Board {
	private int[][] tiles;
	private int N;
	
	public Board(int[][] blocks) {
		N = blocks.length;
		tiles = deepCopy(blocks);
		
		
		// construct a board from an N-by-N array of blocks
		// (where blocks[i][j] = block in row i, column j)
		
		//System.out.println(this);
		//System.out.println("Hamming:"+hamming());
		//System.out.println("Manhattan:"+manhattan());
	}

	public int dimension() {
		return N;
		// board dimension N
	}
	
	public int hamming() {
		int hamming = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tiles[i][j] != 0 && tiles[i][j] != i*N+j+1) {
					hamming++;
					//System.out.println("Wrong Tile["+i+"]["+j+"], Hamming, i*N+j+1="+(i*N+j+1));
				}
					
			}
		}
		return hamming;
		// number of blocks out of place
	}
	
	public int manhattan() {
		int manhattan = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tiles[i][j] != 0 && tiles[i][j] != i*N+j+1) {
					manhattan += Math.abs((tiles[i][j]-1)/N-i)+Math.abs((tiles[i][j]-1)%N-j);
					//System.out.println("Wrong Tile["+i+"]["+j+"], ManNum="+Math.abs((tiles[i][j]-1)/N-i)+" + "+Math.abs((tiles[i][j]-1)%N-j));
					//System.out.println("tile/N="+((tiles[i][j]-1)/N)+" tile%N="+((tiles[i][j]-1)%N));
				}
			}
		}
		return manhattan;
		// sum of Manhattan distances between blocks and goal
	}
	
	public boolean isGoal() {
		if (manhattan() == 0) return true;
		return false;
	}
	
	public Board twin() {
		int i,j;
		do {
			i= StdRandom.uniform(N);
			j = StdRandom.uniform(N-1);
		} while (tiles[i][j] == 0 || tiles[i][j+1] == 0);
		return new Board(swapedArray(i, j, i, j+1));
		// a board obtained by exchanging two adjacent blocks in the same row
	}
	
	private int[][] deepCopy(int[][] input) {
		int[][] deepCopy = new int[N][N];
		for (int i = 0; i < N; i++) 
			for (int j = 0; j < N; j++)
				deepCopy[i][j] = input[i][j];
		return deepCopy;
	}
	
	private int[][] swapedArray(int x1, int y1, int x2, int y2) {
		int[][] newNeighbor = deepCopy(tiles);
		int swap = newNeighbor[x1][y1];
		newNeighbor[x1][y1] = newNeighbor[x2][y2];
		newNeighbor[x2][y2] = swap;
		return newNeighbor;
	}
	
	private void addNeighbor(java.util.Stack<Board> neighbors, int x1, int y1, int x2, int y2) {
		Board nei = new Board(swapedArray(x1, y1, x2, y2));
		neighbors.add(nei);
	}
	
	public Iterable<Board> neighbors() {
		java.util.Stack<Board> neighbors = new java.util.Stack<Board>();
		if (neighbors.isEmpty()) {
			for (int i = 0; i < N; i++) 
				for (int j = 0; j < N; j++) {
					if (tiles[i][j] == 0) {
						if (i != 0) addNeighbor(neighbors, i, j, i-1, j);
						if (i != N-1) addNeighbor(neighbors, i, j, i+1, j);
						if (j != 0) addNeighbor(neighbors, i, j, i, j-1);
						if (j != N-1) addNeighbor(neighbors, i, j, i, j+1);
					}
				}
		}
		//System.out.println("Number of Neighbours:"+neighbors.size());
		return neighbors;
		// all neighboring boards
	}
	
	public boolean equals(Object board) {
		if (board == null) return false;
		if (board == this) return true;
		if (board.getClass() != this.getClass()) return false;
		if (board.toString().length() != this.toString().length()) return false;
		try {
			Board that = (Board) board;
			for (int i = 0; i < N; i++) 
				for (int j = 0; j < N; j++)
					if(tiles[i][j] != that.tiles[i][j]) return false;
		} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
			return false;
		}
		return true;
		
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(N + "\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				s.append(String.format("%2d ", tiles[i][j]));
			}
			s.append("\n");
		}
		return s.toString();
		// string representation of the board (in the output format specified }
	}
}