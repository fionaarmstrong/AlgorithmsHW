public class Solver {
	private MinPQ<Node> queue;
	private MinPQ<Node> twinQueue;
	private int moves;
	private boolean isSolvable;
	private java.util.LinkedList<Board> solution;

	private class Node {
		private Node previous;
		private Board board;
		private java.util.Stack<Node> neighbors;
		private int pathMoves;
		
		public Node(Board bd) {
			board = bd;
			neighbors = new java.util.Stack<Node>();
			pathMoves = 0;
		}
		
		public Node(Board bd, Node pre, int preMoves) {
			board = bd;
			neighbors = new java.util.Stack<Node>();
			previous = pre;
			pathMoves = preMoves+1;
		}
		
		public Node previous() {
			return previous;
		}
		
		public Iterable<Node> neighbors() {
			java.util.Iterator<Board> it = board.neighbors().iterator();
			while (it.hasNext()) {
				Board nextBoard = it.next();
				if(previous != null) 
					if (!nextBoard.equals(previous.board())) {
						this.neighbors.push(new Node(nextBoard, this, this.pathMoves));
					}
				if(previous == null) {
					this.neighbors.push(new Node(nextBoard, this, this.pathMoves));
				}
			}
			return this.neighbors;
		}
		
		public Board board() {
			return board;
		}
		
		public int pathSum() {
			return board.manhattan()+pathMoves;
		}
	}
	
	public Solver(Board initial) {
		queue = new MinPQ<Node>(new ManhattanComparator());
		queue.insert(new Node(initial));
		twinQueue = new MinPQ<Node>(new ManhattanComparator());
		//System.out.println(initial);
		//System.out.println(initial.twin());
		twinQueue.insert(new Node(initial.twin()));
		solution = new java.util.LinkedList<Board>();

		
		while (!queue.isEmpty()) {
			Node current = queue.delMin();
			Node twinCurrent = twinQueue.delMin();
			//Reconstructing solution:
			if (current.board().isGoal()) {
				isSolvable = true;
				solution.add(current.board());
				while (current.previous() != null) {
					current = current.previous();
					solution.addFirst(current.board());
					moves++;
				}
				break;
			}
			
			java.util.Iterator<Node> itsNeighbors = current.neighbors().iterator();
			while (itsNeighbors.hasNext()) {
				queue.insert(itsNeighbors.next());
			}
			
			java.util.Iterator<Node> itsTwinsNeighbors = twinCurrent.neighbors().iterator();
			while (itsTwinsNeighbors.hasNext()) {
				twinQueue.insert(itsTwinsNeighbors.next());
			}
			if (twinCurrent.board().isGoal()) {
				isSolvable = false;
				moves = -1;
				return;
			}
		}
		// find a solution to the initial board (using the A* algorithm)
	}
	
	private static class ManhattanComparator implements java.util.Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) {
			if (o1.pathSum() == o2.pathSum()) return 0;
			return (o1.pathSum() > o2.pathSum())?1:-1;
		}
		
	}
	
	public boolean isSolvable() {
		return isSolvable;
		// is the initial board solvable?
	}
	
	public int moves() {
		return moves;
		// min number of moves to solve initial board; -1 if no solution
	}
	
	public Iterable<Board> solution() {
		if (isSolvable) return solution;
		else return null;
		// sequence of boards in a shortest solution; null if no solution
	}
	
	public static void main(String[] args) {
		// solve a slider puzzle (given below)
		// create initial board from file
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		
		Board initial = new Board(blocks);
		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}