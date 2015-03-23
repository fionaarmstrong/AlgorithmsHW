/****************************************************************************
 *  Author:        Guoguo Lu
 *  Written:       9/25/2013
 *  Last updated:  9/25/2013
 *  
 *  Compilation:  javac Percolation.java
 *  Execution:  java Percolation N
 *  Dependencies: WeightedQuickUnionUF.java StdIn.java StdOut.java
 *
 *  HW1 2/2
 *  http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *  
 ****************************************************************************/
public class Percolation {
private int N;
private WeightedQuickUnionUF uf;
private int topID; //, bottomID;
private boolean[] isOpen;
private boolean[] isFull;
private boolean percolates;

public Percolation(int N) {
this.N = N;
topID = N*N;
//bottomID = N*N+1;
uf = new WeightedQuickUnionUF(N*N+2); 
isOpen = new boolean[N*N];
isFull = new boolean[N*N];
}

public void open(int i, int j) {
if (i < 1 || i > N || j < 1 || j > N) throw new IndexOutOfBoundsException(); 
if (isOpen(i, j)) return; 
if (i == 1) {
	uf.union(convertID(i, j), topID);
	isFull[convertID(i, j)] = true;
	fillNeighbor(i, j);
}
isOpen[convertID(i, j)] = true;
 
checkNeighbor(i, j);
}
   
private void checkNeighbor(int i, int j) {
	if (i != 1) if (isOpen(i-1, j)) {
		uf.union(convertID(i, j), convertID(i-1, j));
		if (isFull(i-1, j)) {
			isFull[convertID(i, j)] = true;
			fillNeighbor(i, j);
		}
	}
	if (i != N) if (isOpen(i+1, j)) {
		uf.union(convertID(i, j), convertID(i+1, j));
		if (isFull(i+1, j)){
			isFull[convertID(i, j)] = true;
			fillNeighbor(i, j);
		}
	}
	if (j != 1) if (isOpen(i, j-1)) {
		uf.union(convertID(i, j), convertID(i, j-1));
		if (isFull(i, j-1)) {
			isFull[convertID(i, j)] = true;
			fillNeighbor(i, j);
		}
	}
	if (j != N) if (isOpen(i, j+1)) {
		uf.union(convertID(i, j), convertID(i, j+1));
		if (isFull(i, j+1)) {
			isFull[convertID(i, j)] = true;
			fillNeighbor(i, j);
		}
	}
}

private void fillNeighbor(int i, int j) {
	if (i < 1 || i > N || j < 1 || j > N) return;
	else if (!isOpen(i, j) || !isFull(i, j)) return;
	
	if (i !=1) if (isOpen(i-1, j) && !isFull(i-1, j)) {
		isFull[convertID(i-1, j)] = true;
		fillNeighbor(i-1, j);
	}
	if (i !=N) if (isOpen(i+1, j) && !isFull(i+1, j)) {
		isFull[convertID(i+1, j)] = true;
		fillNeighbor(i+1, j);
	}
	if (j !=1) if (isOpen(i, j-1) && !isFull(i, j-1)) {
		isFull[convertID(i, j-1)] = true;
		fillNeighbor(i, j-1);
	}
	if (j !=N) if (isOpen(i, j+1) && !isFull(i, j+1)) {
		isFull[convertID(i, j+1)] = true;
		fillNeighbor(i, j+1);
	}
}

public boolean isOpen(int i, int j) {
if (i < 1 || i > N || j < 1 || j > N) throw new IndexOutOfBoundsException(); 
return isOpen[convertID(i, j)];
}
   
public boolean isFull(int i, int j) {
if (i < 1 || i > N || j < 1 || j > N) throw new IndexOutOfBoundsException();  
return isFull[convertID(i, j)];
/*if (isFull[convertID(i, j)]) return true;
else {
	if (uf.connected(convertID(i, j), topID)) {
	isFull[convertID(i, j)] = true;
	return true;
}
}
return false;*/
}

public boolean percolates() {
if (this.percolates) return true;
int index = uf.find(topID);
for (int i = 0; i < N; i++) {
	if (isOpen[N*(N-1)+i]) {
	if (uf.find(N*(N-1)+i) == index) {
		this.percolates = true;
		return true;
	}
	}
}
return false;
}

public static void main(String[] args){
	int N=5;
	Percolation perco = new Percolation(N);
	int i, j;

	while (!perco.percolates()) {
	do {
	i = 1+StdRandom.uniform(N);
	j = 1+StdRandom.uniform(N);
	}
	while(perco.isOpen(i, j));
	perco.open(i, j);
	System.out.println("i="+i+" j="+j);
	
	System.out.println("-------------------IsOpen?-----------------");
	for(i = 1; i <= N; i++){
		
		for(j = 1; j <= N; j++){
			if(perco.isOpen(i,j)) System.out.print("È¦");
			else System.out.print("¿Ú");
		}
		System.out.println("");
	}
	System.out.println("-----------IsFull?-----------");
	for(i = 1; i <= N; i++){
		
		for(j = 1; j <= N; j++){
			if(perco.isFull(i,j)) System.out.print("‡å");
			else System.out.print("¿Ú");
		}
		System.out.println("");
	}
	System.out.println("");
	
	}

	
}

private int convertID(int i, int j) {
return (i-1)*N+j-1;
}
}