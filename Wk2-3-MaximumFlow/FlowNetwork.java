/**
 * Ford-Fulkerson MaxFlow-MinCut Implementation
 * Code from the lectures
 * @author Fiona Lu
 * @date 04/21/2015
 */
public class FlowNetwork {
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
}
