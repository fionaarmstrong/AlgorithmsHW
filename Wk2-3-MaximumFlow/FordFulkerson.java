/**
 * Ford-Fulkerson MaxFlow-MinCut Implementation
 * Code from the lectures
 * @author Fiona Lu
 * @date 04/21/2015
 */
public class FordFulkerson {
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
