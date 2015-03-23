import java.lang.reflect.Array;
import java.util.*;


public class SAP {
	private Digraph dig;
	// cache key is the v, w pair with increasing order
	// cache value is int[]{length, ancestor}
	private HashMap<int[], int[]> cache;
	private HashMap<KeyPair, int[]> cacheIterable;
	private static final int CACHE_LENGTH = 0;
	private static final int CACHE_ANCESTOR = 1;
	
	private class KeyPair {
		public Iterable<Integer> v;
		public Iterable<Integer> w;
		
		public KeyPair(Iterable<Integer> v, Iterable<Integer> w) {
			this.v = v;
			this.w = w;
		}
		
		@Override
		public boolean equals(Object arg) {
			if (arg == null) return false;
			if (this == arg) return true;
			if (!(arg instanceof KeyPair)) return false;
			KeyPair that = (KeyPair) arg;
			if (this.v.equals(that.v) 
					&& this.w.equals(that.w))
				return true;
			else if (this.v.equals(that.w)
					&& this.w.equals(that.v))
				return true;
			else return false;
		}
		
		@Override
		public int hashCode() {
			return v.hashCode() + 31 * w.hashCode();
			
		}
	}
	
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		dig = new Digraph(G);
		cache = new HashMap<int[], int[]>();
		cacheIterable = new HashMap<KeyPair, int[]>();
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		if (v < 0 || v >= dig.V() || w < 0 || w >= dig.V())  throw new java.lang.IndexOutOfBoundsException();
		if (v == w) return 0;
		
		int[] cacheKey = new int[] {Math.min(v, w), Math.max(v, w)};
		if (!cache.containsKey(cacheKey)) {
			cache.put(cacheKey, lengthAncestorSearch(cacheKey));
		}
		//StdOut.println("cacheValue  " + cache.get(cacheKey)[0] + "  " + cache.get(cacheKey)[1]);
		return cache.get(cacheKey)[CACHE_LENGTH];
		
		/* Method call to recursively search one ancestor.
		boolean[] visited = new boolean[dig.V()];
		return searchParents(v, w, visited);*/
	}
	
	
	/* Deplicated - This method will only search one path, but not the minimum.
	private int searchParents(int v, int w, boolean[] visited) {
		int recursion = searchAllChildren(v, w, visited);
		
		if (recursion != -1) {
			return recursion;
		}
		
		for (int parent : dig.adj(v)) {
			System.out.println("Visiting parent:" + parent);
			visited[parent] = true;
			recursion = searchParents(parent, w, visited);
			if (recursion != -1) {
				System.out.println("Recursion found:" + recursion + ",  in parent " + parent);
				return recursion+1;
			}
		}
		
		return -1;
	}

	private int searchAllChildren(int v, int w, boolean[] visited) {
		visited[v] = true;
		if (v == w) 
			return 0;
		
		int recursion = 0;
		for (int child : revDig.adj(v)) {
			System.out.println("Visiting child:" + child);
			if (visited[child]) continue;
			recursion = searchAllChildren(child, w, visited);
			if (recursion != -1) {
				System.out.println("Recursion found:" + recursion + ",  in child " + child);
				return recursion + 1;
			}
				
		}
		
		return -1;
	}
	*/
	
	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		if (v < 0 || v >= dig.V() || w < 0 || w >= dig.V())  throw new java.lang.IndexOutOfBoundsException();
		if (v == w) return v;
		
		int[] cacheKey = new int[] {Math.min(v, w), Math.max(v, w)};
		if (!cache.containsKey(cacheKey)) {
			cache.put(cacheKey, lengthAncestorSearch(cacheKey));
		}
		//StdOut.println("cacheValue  " + cache.get(cacheKey)[0] + "  " + cache.get(cacheKey)[1]);
		return cache.get(cacheKey)[CACHE_ANCESTOR];
	}

	private int[] lengthAncestorSearch(int[] cacheKey) {
		int v = cacheKey[0];
		int w = cacheKey[1];
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(dig, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(dig, w);
		
		int minLength = Integer.MAX_VALUE;
		int minAncestor = -1;
		for (int i = 0; i < dig.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)
					&& bfsW.distTo(i) + bfsV.distTo(i) < minLength) {
				minLength = bfsW.distTo(i) + bfsV.distTo(i);
				minAncestor = i;
			}
		}
		if (minLength == Integer.MAX_VALUE) minLength = -1;
		return new int[]{minLength, minAncestor};
	}
	
	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		for (int i: v)
			if (i < 0 || i >= dig.V()) throw new java.lang.IndexOutOfBoundsException();
		for (int i: w)
			if (i < 0 || i >= dig.V()) throw new java.lang.IndexOutOfBoundsException();
		
		KeyPair cacheKey = new KeyPair(v, w);
		
		if (!cacheIterable.containsKey(cacheKey)) {
			cacheIterable.put(cacheKey, lengthAncestorSearch(cacheKey));
		}
		return cacheIterable.get(cacheKey)[CACHE_LENGTH];
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		for (int i: v)
			if (i < 0 || i >= dig.V()) throw new java.lang.IndexOutOfBoundsException();
		for (int i: w)
			if (i < 0 || i >= dig.V()) throw new java.lang.IndexOutOfBoundsException();
		
		KeyPair cacheKey = new KeyPair(v, w);
		
		if (!cacheIterable.containsKey(cacheKey)) {
			cacheIterable.put(cacheKey, lengthAncestorSearch(cacheKey));
		}
		return cacheIterable.get(cacheKey)[CACHE_ANCESTOR];
	}

	private int[] lengthAncestorSearch(KeyPair cacheKey) {
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(dig, cacheKey.v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(dig, cacheKey.w);
		int minLength = Integer.MAX_VALUE;
		int minAncestor = -1;
		for (int i = 0; i < dig.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)
					&& bfsW.distTo(i) + bfsV.distTo(i) < minLength) {
				minLength = bfsW.distTo(i) + bfsV.distTo(i);
				minAncestor = i;
			}
		}
		if (minLength == Integer.MAX_VALUE) minLength = -1;
		return new int []{minLength, minAncestor};
	}
	
	// for unit testing of this class (such as the one below)
	public static void main(String[] args) {
		In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {	
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
	}
}
