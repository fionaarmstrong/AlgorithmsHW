public class Brute {
   public static void main(String[] args) {
	   In in = new In(args[0]);
	   int numberOfPoints = in.readInt();
	   Point[] point = new Point[numberOfPoints];
	   for (int i = 0; i < numberOfPoints; i++) {
		   point[i] = new Point(in.readInt(), in.readInt());
		   point[i].draw();
	   }
	   java.util.Arrays.sort(point);
	   StdDraw.setXscale(0, 32768);
	   StdDraw.setYscale(0, 32768);
	   
	   for (int p = 0; p < numberOfPoints; p++) {
		   for (int q = p+1; q < numberOfPoints; q++) {
			   
			   for (int r = q+1; r < numberOfPoints; r++) {
				   if (point[p].slopeTo(point[q]) == point[p].slopeTo(point[r]) ) {
					   for (int s = r+1; s < numberOfPoints; s++) {
						   //System.out.println("Comparison --------------------------");
						   //System.out.println(point[p].slopeTo(point[q]) + point[p].toString()+" -> "+point[q].toString());
						   //System.out.println(point[p].slopeTo(point[r]) + point[p].toString()+" -> "+point[r].toString());
						   //System.out.println(point[p].slopeTo(point[s]) + point[p].toString()+" -> "+point[s].toString());
						   if (point[p].slopeTo(point[q]) == point[p].slopeTo(point[s])) {
							   StdOut.println(point[p]+" -> "+point[q]+" -> "+point[r]+" -> "+point[s]);
							   point[p].drawTo(point[s]);
						   } //End of If
					   } //End of for s
				   } //End of If
			   }
		   }
	   }   
   }
}