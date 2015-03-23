/*************************************************************************
 * Name:Guoguo Lu
 * Email:snowsguoguo@hotmail.com
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopComparator();

    private final int x;   // x coordinate
    private final int y;   // y coordinate
    
    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
    	if (this.y == that.y && this.x == that.x) return Double.NEGATIVE_INFINITY;
    	
    	if (this.y == that.y) return 0;
    	else if (this.x == that.x) return Double.POSITIVE_INFINITY;
        return ((double)that.y-(double)this.y)/((double)that.x-(double)this.x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y == that.y) return this.x - that.x;
        else return this.y - that.y;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    private class SlopComparator implements Comparator<Point> {
		@Override
		public int compare(Point p1, Point p2) {
			return (slopeTo(p1) > slopeTo(p2))?1:-1;
		}
    }
    
    // unit test
    public static void main(String[] args) throws InterruptedException {
        Point pt1 = new Point(1, 1);
        Point pt2 = new Point(0, 1);
        Point pt3 = new Point(0, 0);
        System.out.println(pt1.toString());
        System.out.println(pt2.toString());
        System.out.println(pt2.compareTo(pt1));
        System.out.println(pt1.slopeTo(pt2));
        System.out.println(pt1.slopeTo(pt3));
    }
}