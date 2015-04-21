public class Fast {
	private Point[] point;
	private int numberOfPoints;
	private In in;
	private int[] findEquals(int i, int j, int last) {
		for (int k = j; k < last; k++) {
			int result = recursiveFind(i, k, last);
			if (result != k) {
				if (result - k >= 2) return new int[]{k, result};
				else k = result;
			}
		}
		return null;
	}
	
	//Return the last non equal index
	private int recursiveFind(int i, int x,int last) {
		if (x == last-1) return x;
		if (point[i].slopeTo(point[x]) == point[i].slopeTo(point[x+1])) {
			return recursiveFind(i, x+1, last);
		}
		else return x;
	}
	
	private void openFile(String textfile) {
		in = new In(textfile);
	       numberOfPoints = in.readInt();
		   point = new Point[numberOfPoints];
		   for (int i = 0; i < numberOfPoints; i++) {
			   point[i] = new Point(in.readInt(), in.readInt());
			   point[i].draw();
		   }
		   
		   StdDraw.setXscale(0, 32768);
		   StdDraw.setYscale(0, 32768);	   
		   
		   java.util.Arrays.sort(point);
		   for (int i = 0; i < point.length; i++) {
			   java.util.Arrays.sort(point,i,point.length);
			   java.util.Arrays.sort(point,i+1,point.length,point[i].SLOPE_ORDER);
			   int[] collinear;
			   int start = i+1;
			   int end;
			   while ((collinear = findEquals(i, start, point.length)) != null) {
				   start = collinear[0];
				   end = collinear[1];
				   StdOut.print(point[i] + " -> ");
				   for (; start < end; start++) {
					   StdOut.print(point[start] + " -> ");
				   }
				   StdOut.println(point[end]);
				   point[i].drawTo(point[end]);
			   }
		   }
		
	}
	
	public Fast() {

	}
	
	public static void main(String[] args) {
		Fast fast = new Fast();
		fast.openFile(args[0]);
		
	}
}
