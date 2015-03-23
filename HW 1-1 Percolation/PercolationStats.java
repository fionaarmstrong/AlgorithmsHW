/****************************************************************************
 *  Author:        Guoguo Lu
 *  Written:       9/25/2013
 *  Last updated:  9/25/2013
 *  
 *  Compilation:  javac PercolationStats.java
 *  Execution:  java PercolationStats N T
 *  Dependencies: Percolation.java WeightedQuickUnionUF.java StdIn.java StdOut.java
 *
 *  HW1 1/2
 *  http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 ****************************************************************************/


public class PercolationStats {
private int[] threshold;
private int N;
private int T;

public PercolationStats(int N, int T) {
if (N <= 0 || T <= 0) throw new IllegalArgumentException();
threshold = new int[T];
this.N = N;
this.T = T;
for (int k = 0; k < T; k++) {
Percolation perco = new Percolation(N);
int i, j;
int count = 0;

while (!perco.percolates()) {
do {
i = 1+StdRandom.uniform(N);
j = 1+StdRandom.uniform(N);
}
while (perco.isOpen(i, j));

perco.open(i, j);
count++;
}

this.threshold[k] = count;
}
StdOut.println("mean                    = "+mean());
StdOut.println("stddev                  = "+stddev());
StdOut.println("95% confidence interval = "+confidenceLo()+", "+confidenceHi());
}
   
public double mean() {
return StdStats.mean(threshold)/(N*N);
}
   
public double stddev() {
return StdStats.stddev(threshold)/(N*N);
}
   
public double confidenceLo() {
if (T <= 30) {
return Double.NaN;
}
return mean()-1.96*stddev()/Math.sqrt(T);
}
   
public double confidenceHi() {
if (T <= 30) {
return Double.NaN;
}
return mean()+1.96*stddev()/Math.sqrt(T);
}

public static void main(String[] args) {
try {
new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
} catch (java.lang.NumberFormatException ee) {
StdOut.println("Please enter two numbers");
return;
}
}
}
