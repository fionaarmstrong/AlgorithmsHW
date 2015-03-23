/*************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: StdDraw.java Point2D.java KdTree.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 *************************************************************************/

public class KdTreeVisualizer {

    public static void main(String[] args) {
        StdDraw.show(0);
        KdTree kdtree = new KdTree();
        while (true) {
            if (StdDraw.mousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                Point2D p = new Point2D(x, y);
                if (x > 0.0 && x < 1.0 
                		&& y > 0.0 && y < 1.0
                		&& !kdtree.contains(p)) {
	                System.out.printf("%8.6f %8.6f Size:+%d\n", x, y, kdtree.size()); 
	                kdtree.insert(p);
	                StdDraw.clear();
	                kdtree.draw();
                }
            }
            StdDraw.show(2);
        }

    }
}