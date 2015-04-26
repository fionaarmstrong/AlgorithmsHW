/**
 * Baseball Elimination HW
 * @author Fiona Lu
 * @date 04/26/2015
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BaseballElimination {
	private class FlowEdge {
		private final int v, w;
		private final double capacity;
		private double flow;

		public FlowEdge(int v, int w, double capacity){
			this.v = v;
			this.w = w;
			this.capacity = capacity;
		}
		
		public int from() { return this.v; }
		
		public int to() { return this.w; }
		
		public double capacity() { return this.capacity; }
		
		public double flow() { return this.flow; }
		
		/*
		 * Other endpoint
		 */
		public int other(int vertex) {
			if (vertex == v) return w;
			else if (vertex == w) return v;
			else throw new RuntimeException("Illegal endpoint");
		}
		
		public double residualCapacityTo(int vertex) {
			// Backward edge
			if (vertex == v) 
				return flow;
			// Forward edge
			else if (vertex == w) 
				return capacity - flow;
			else 
				throw new RuntimeException("Illegal endpoint");
		}
		
		public void addResidualFlowTo(int vertex, double delta) {
			// Backward edge
			if (vertex == v) 
				flow -= delta;
			// Forward edge
			else if (vertex == w) 
				flow += delta;
			else 
				throw new RuntimeException("Illegal endpoint");
		}
		
		@Override
		public String toString() {
			return v + "->" + w + " " + flow + "/" + capacity;
		}
	}
	
	private class FlowNetwork {
		private final int V;
		private Bag<FlowEdge>[] adj;
		/*
		 * Create an empty flow network with V vertices
		 */
		@SuppressWarnings("unchecked")
		public FlowNetwork(int V) {
			this.V = V;
			adj = (Bag<FlowEdge>[]) new Bag[V];
			for (int v = 0; v < V; v++)
				adj[v] = new Bag<FlowEdge>();
		}
		
		/**
		 * Add flow edge e to this flow network
		 */
		public void addEdge(FlowEdge e) {
			int v = e.from();
			int w = e.to();
			adj[v].add(e);
			adj[w].add(e);
		}
		
		/*
		 * Forward and backward edges adjacent to v
		 */
		public Iterable<FlowEdge> adj(int v) {
			return adj[v];
		}
		
		public int V() { return V; }
		
		@Override
	    public String toString() {
	        String NEWLINE = System.getProperty("line.separator");
	        StringBuilder s = new StringBuilder();
	        for (int v = 0; v < V; v++) {
	            s.append(v + ":  ");
	            for (FlowEdge e : adj[v]) {
	                if (e.to() != v) s.append(e + "  ");
	            }
	            s.append(NEWLINE);
	        }
	        return s.toString();
	    }
	}
	
	private class FordFulkerson {
		// True if s->v path in residual network
		private boolean[] marked;
		// Last edge on s-> path
		private FlowEdge[] edgeTo;
		// Value of flow
		private double value;
		
		public FordFulkerson(FlowNetwork G, int s, int t) {
			value = 0.0;
			while (hasAugmentingPath(G, s, t)) {
				double bottle = Double.POSITIVE_INFINITY;
				for (int v = t; v != s; v = edgeTo[v].other(v))
					bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
				
				for (int v = t; v != s; v = edgeTo[v].other(v))
					edgeTo[v].addResidualFlowTo(v, bottle);
				
				value += bottle;
			}
		}
		
		private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
			edgeTo = new FlowEdge[G.V()];
			marked = new boolean[G.V()];
			
			Queue<Integer> q = new Queue<Integer>();
			q.enqueue(s);
			marked[s] = true;
			while (!q.isEmpty()) {
				int v = q.dequeue();
				
				for (FlowEdge e: G.adj(v)) {
					int w = e.other(v);
					if (e.residualCapacityTo(w) > 0 && !marked[w]) {
						edgeTo[w] = e;
						marked[w] = true;
						q.enqueue(w);
					}
				}
			}
			return marked[t];
		}
		
		public double value() { return value; }
		
		public boolean inCut(int v) { return marked[v]; }
	}
	
	
	/**
	 * Member variables
	 */
	
	private String[] teams;
	private HashMap<String, Integer> teamIdx;
	private int[] wins;
	private int[] losses;
	private int[] remaining;
	private int[][] gameVerticesCapacity;
	private int[][] teamVerticesCapacity; // teamVP[0] builds the graph for team 0
	private boolean[] isOut;
	private ArrayList<ArrayList<String>> isOutBy;
	
	public BaseballElimination(String filename) {
		// create a baseball division from given filename in format specified below
		In in = new In(filename);
		int len = in.readInt();
		
		teams = new String[len];
		teamIdx = new HashMap<String, Integer>();
		wins = new int[len];
		losses = new int[len];
		remaining = new int[len];
		gameVerticesCapacity = new int[len][len];
		teamVerticesCapacity = new int[len][len];
		isOut = new boolean[len];
		isOutBy = new ArrayList<ArrayList<String>>(len);
		
		int idx = 0;
		
		while (in.hasNextLine()) {
			teams[idx] = in.readString();
			teamIdx.put(teams[idx], idx);
			
			wins[idx] = in.readInt();
			losses[idx] = in.readInt();
			remaining[idx] = in.readInt();
			
			for (int i = 0; i < len; i++) {
				gameVerticesCapacity[idx][i] = in.readInt();
			}
			in.readLine();
			idx++;
		}
		
		for (int i = 0; i < len; i++) {
			isOutBy.add(null);
		}
		
//		for (String s: teamIdx.keySet()) {
//			System.out.print(s);
//			int i = teamIdx.get(s);
//			System.out.print(" w " + wins[i]);
//			System.out.print(" l " + losses[i]);
//			System.out.print(" r " + remaining[i]);
//			for (int j = 0; j < len; j++) {
//				System.out.print(" " + gameVerticesCapacity[i][j]);
//			}
//			System.out.println();
//		}
		
		computeTrivialElimination();
		computeNonTrivialElimination();
	}
	
	private void computeTrivialElimination() {
		int[] sortedWins = wins.clone();
		int[] sortedIdx = new int[wins.length];
		for (int i = 0; i < sortedIdx.length; i++) {
			sortedIdx[i] = i;
		}
		
//		for (int i: sortedWins) {
//			System.out.print(i +" ");
//		}
//		System.out.println();
//		for (int i: sortedIdx) {
//			System.out.print(i +" ");
//		}
//		System.out.println();
		
		// Sort array wins while keeping the original index
		quickSortWithIndex(sortedWins, sortedIdx, 0, sortedWins.length-1);
		
//		for (int i: sortedWins) {
//			System.out.print(i +" ");
//		}
//		System.out.println();
//		for (int i: sortedIdx) {
//			System.out.print(i +" ");
//		}
//		System.out.println();
		
		for (int i = 0; i < wins.length; i++) {
			if (wins[i] + remaining[i] < sortedWins[sortedWins.length-1]) {
				isOut[i] = true;
				int j = sortedWins.length-1;
				int wr = wins[i] + remaining[i];
				while (wr < sortedWins[j]) {
					if (isOutBy.get(i) == null)
						isOutBy.set(i, new ArrayList<String>());
					isOutBy.get(i).add(teams[sortedIdx[j]]);
					j--;
				}
			}
		}
		
	}
	
	private void quickSortWithIndex(int[] a, int[] aIdx, int lo, int hi) {
//		System.out.println("QuickSortAt lo=" + lo + " hi=" + hi);
//		for (int i: a) {
//			System.out.print(i + " ");
//		}
//		System.out.println();
		
		if (lo >= hi) return;
		int lt = lo;
		int gt = hi;
		int i = lo;
		int v = a[lo];
		
		while (i <= gt) {
			if (a[i] < v) {
				swap(a, lt, i);
				swap(aIdx, lt, i);
				lt++;
				i++;
			} else if (a[i] > v) {
				swap(a, i, gt);
				swap(aIdx, i, gt);
				gt--;
			} else i++;
		}
		
		quickSortWithIndex(a, aIdx, lo, lt-1);
		quickSortWithIndex(a, aIdx, gt+1, hi);
		assert isSorted(a, lo, hi);
	}
	
	private boolean isSorted(int[] a, int lo, int hi) {
		for (int i = lo + 1; i <= hi; i++) {
			if (a[i-1] > a[i]) return false;
		}
		return true;
	}
	
	private void swap(int[] a, int i, int j) {
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}
	
	private void computeNonTrivialElimination() {
		computeTeamVerticesCapacity();
		
		for (int i = 0; i < wins.length; i++)
			computeMaxFlowForOneTeam(i);
		
		
	}
	
	private void computeTeamVerticesCapacity() {
		// Compute teamVertices values for all graphs
		// There's n graphs for n teams
		// teamVerticesCapacity[0] have values for team 0
		for (int t = 0; t < wins.length; t++) {
			int wr = wins[t] + remaining[t];
			for (int i = 0; i < wins.length; i++) {
				teamVerticesCapacity[t][i] = wr - wins[i];
			}
		}
	}
	
	private void computeMaxFlowForOneTeam(int team) {
		if (isOut[team] == true) return;
		
		// gameVerticesCapacity
		// teamVerticesCapacity[team]
		
		// number of team vertices n
		// n = number of teams
		// number of game vertices (including current team)
		// nC2 = n!/(2!*(n-2)!) = n*(n-1)/2
		// total number of vertices
		// = 1 for s
		// + 1 for t
		// + teamVertices + gameVertices
		
		int n = wins.length;
		int m = 2 + n-1 + (n-1)*(n-2)/2;
		FlowNetwork graph = new FlowNetwork(m);
		
		for (int i = 0; i < n; i++) {
			if (i == team) continue;

			graph.addEdge(new FlowEdge(getTeamIdx(team, i), m-1, teamVerticesCapacity[team][i]));
		}

		int k = 1;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				if (i == team || j == team) continue;
				
				graph.addEdge(new FlowEdge(0, k, gameVerticesCapacity[i][j]));
				graph.addEdge(new FlowEdge(k, getTeamIdx(team, i), Double.POSITIVE_INFINITY));
				graph.addEdge(new FlowEdge(k, getTeamIdx(team, j), Double.POSITIVE_INFINITY));
				k++;
			}
		}
		
//		for (int i = 0; i < n; i++) {
//			System.out.print(teamVerticesCapacity[team][i] + " ");
//		}
//		System.out.println("m=" + m);
//		
		//System.out.println(graph.toString());
		
		FordFulkerson ff = new FordFulkerson(graph, 0, m-1);
		
		//System.out.println(graph.toString());
		
		for (FlowEdge e:graph.adj(0)) {
			if ( e.flow != e.capacity ) {
				isOut[team] = true;
				for (int i = 0; i < n; i++) {
					if (i == team) continue;
					
					if (ff.inCut(getTeamIdx(team, i))) {
						if (isOutBy.get(team) == null)
							isOutBy.set(team, new ArrayList<String>());
						isOutBy.get(team).add(teams[i]);
					}
				}
			}
		}
	}
	
	private int getTeamIdx(int team, int i) {
		int n = wins.length;
		if (i < team)
			return i + 1 + (n-1)*(n-2)/2;
		else if (i > team)
			return i + (n-1)*(n-2)/2;
		else return -1;
	}
	
	public int numberOfTeams() {
		// number of teams
		return wins.length;
	}
	
	public Iterable<String> teams() {
		// all teams
		return Arrays.asList(teams);
	}
	
	public int wins(String team) {
		// number of wins for given team
		if (!teamIdx.containsKey(team))
			throw new IllegalArgumentException();
		return wins[teamIdx.get(team)];
	}
	
	public int losses(String team) {
		// number of losses for given team
		if (!teamIdx.containsKey(team))
			throw new IllegalArgumentException();
		return losses[teamIdx.get(team)];
	}
	
	public int remaining(String team) {
		// number of remaining games for given team
		if (!teamIdx.containsKey(team))
			throw new IllegalArgumentException();
		return remaining[teamIdx.get(team)];
	}
	
	public int against(String team1, String team2) {
		// number of remaining games between team1 and team2
		if (!teamIdx.containsKey(team1) || !teamIdx.containsKey(team2))
			throw new IllegalArgumentException();
		return gameVerticesCapacity[teamIdx.get(team1)][teamIdx.get(team2)];
	}
	
	public boolean isEliminated(String team) {
		// is given team eliminated?
		if (!teamIdx.containsKey(team))
			throw new IllegalArgumentException();
		return isOut[teamIdx.get(team)];
	}
	
	public Iterable<String> certificateOfElimination(String team) {
		// subset R of teams that eliminates given team; null if not eliminated
		if (!teamIdx.containsKey(team))
			throw new IllegalArgumentException();
		return isOutBy.get(teamIdx.get(team));
	}
	
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination("baseball/teams5.txt");
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}
