
public class PointSET {
	private SET<Point2D> list;
	// construct an empty set of points
	public PointSET() {
		list = new SET<Point2D>();
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public int size() {
		return list.size();
	}
	
	public void insert(Point2D p) {
		// add the point p to the set (if it is not already in the public boolean contains(Point2D p) 
		// does the set contain the point p?
		if (!list.contains(p)) {
			list.add(p);
		}
	}
	
	public boolean contains(Point2D p) {
		return list.contains(p);
	}
	
	public void draw() {
		// draw all of the points to standard draw
		for (Point2D p: list) {
			p.draw();
		}
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) throw new NullPointerException();
		java.util.Stack<Point2D> returnList = new java.util.Stack<Point2D>();
		for (Point2D p: list) {
			if (p.x() >= rect.xmin() && p.x() <= rect.xmax() && p.y() >= rect.ymin() && p.y() <= rect.ymax()) {
				returnList.add(p);
			}
		}
		return returnList;
		// all points in the set that are inside the rectangle
	}
	
	public Point2D nearest(Point2D p) {
		if (p == null) throw new NullPointerException();
		if (list.isEmpty()) return null;
		Point2D nearest = list.min();
		for (Point2D pp: list) {
			if (pp.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
				nearest = pp;
			}
		}
		return nearest;
		// a nearest neighbor in the set to p; null if set is empty
	}
}