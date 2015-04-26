/**
 * Ford-Fulkerson MaxFlow-MinCut Implementation
 * Code from the lectures
 * @author Fiona Lu
 * @date 04/21/2015
 */
public class FlowEdge {
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
