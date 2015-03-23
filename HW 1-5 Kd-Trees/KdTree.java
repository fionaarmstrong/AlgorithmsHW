
public class KdTree {
	private Node rootNode = null;
	private int size;
	
	private static class Node {
		private Point2D point; // the point
		private RectHV rect; // the axis-aligned rectangle corresponding to this node
		private Node parent;
		private Node leftChild; // the left/bottom subtree
		private Node rightChild; // the right/top subtree
		private boolean isOddLevel;
		public Node(Point2D p, boolean isOdd) {
			point = p;
			isOddLevel = isOdd;
		}
		
		public void setRect(double xMin, double yMin, double xMax, double yMax) {
			rect = new RectHV(xMin, yMin, xMax, yMax);
		}
		
		public double compare(Point2D p) {
			if (isOddLevel) return point.x()-p.x();
			else return point.y()-p.y();
		}
		
		public Point2D point() {
			return point;
		}
		
		public void setParent(Node newNode) {
			parent = newNode;
			if (isOddLevel) {
				if (parent.leftChild() == this) {
					setRect(parent.rect().xmin(), parent.point().y(), parent.rect().xmax(), parent.rect().ymax());
				}
				else {
					setRect(parent.rect().xmin(), parent.rect().ymin(), parent.rect().xmax(), parent.point().y());
				}
			}
			else {
				if (parent.leftChild() == this) {
					setRect(parent.point().x(), parent.rect().ymin(), parent.rect().xmax(), parent.rect().ymax());
				}
				else {
					setRect(parent.rect().xmin(), parent.rect().ymin(), parent.point().x(), parent.rect().ymax());
				}
			}
		}
		
		public Node rightChild() {
			return rightChild;
		}
		
		public void setRightChild(Node newChild) {
			rightChild = newChild;
		}
		
		public Node leftChild() {
			return leftChild;
		}
		
		public void setLeftChild(Node newChild) {
			leftChild = newChild;
		}
		
		public RectHV rect() {
			return rect;
		}
		
		public boolean isOddLevel() {
			return isOddLevel;
		}
	}
	
	public KdTree() {
		// construct an empty set of points
		size = 0;
	}
	
	public boolean isEmpty() {
		if (rootNode == null) return true;
		return false;
		// is the set empty?
	}
	
	public int size() {
		return size;
		// number of points in the set
	}
	
	public void insert(Point2D p) {
		// add the point p to the set (if it is not already in the public boolean contains(Point2D p) 
		// does the set contain the point p?
		if (p == null) throw new NullPointerException();
		if (rootNode == null) {
			rootNode = new Node(p, true);
			rootNode.setRect(0.0, 0.0, 1.0, 1.0);
			size = 1;
			return;
		}
		Node currentNode = rootNode;
		while (true) {
			double comparison = currentNode.compare(p);
			if (comparison > 0) {
				if (currentNode.rightChild() == null) {
					currentNode.setRightChild(new Node(p, !currentNode.isOddLevel()));
					currentNode.rightChild().setParent(currentNode);
					size++;
					break;
				}
				else {
					currentNode = currentNode.rightChild();
				}
			}
			else if (comparison < 0) { 
				if (currentNode.leftChild() == null) {
					currentNode.setLeftChild(new Node(p, !currentNode.isOddLevel()));
					currentNode.leftChild().setParent(currentNode);
					size++;
					break;
				}
				else {
					currentNode = currentNode.leftChild();
				}
			}
			else { // comparison == 0
				if (currentNode.point().equals(p)) return;
				else if (currentNode.rightChild() == null) {
					currentNode.setRightChild(new Node(p, !currentNode.isOddLevel()));
					currentNode.rightChild().setParent(currentNode);
					size++;
					break;
				}
				else {
					currentNode = currentNode.rightChild();
				}
			}
			
		}
	}
	
	public void draw() {
		if (rootNode == null) return;
		drawRecursive(rootNode);
	}
	
	private static void drawRecursive(Node currentNode) {
		if (currentNode == null) return;
		
		if (currentNode.isOddLevel()) {
			StdDraw.setPenColor(StdDraw.RED);
	        StdDraw.setPenRadius();
	        StdDraw.line(currentNode.point().x(), currentNode.rect().ymin(), currentNode.point().x(), currentNode.rect().ymax());
	        
	        StdDraw.setPenColor(StdDraw.BLACK);
	        StdDraw.setPenRadius(.01);
	        currentNode.point().draw();
		}
		else {
			StdDraw.setPenColor(StdDraw.BLUE);
	        StdDraw.setPenRadius();
	        StdDraw.line(currentNode.rect().xmin(), currentNode.point().y(), currentNode.rect().xmax(), currentNode.point().y());
	        
	        StdDraw.setPenColor(StdDraw.BLACK);
	        StdDraw.setPenRadius(.01);
	        currentNode.point().draw();
		}
		
		drawRecursive(currentNode.leftChild());
		drawRecursive(currentNode.rightChild());
	}
	
	public boolean contains(Point2D p) {
		if (rootNode == null) return false;
		Node currentNode = rootNode;
		while (currentNode != null) {
			double comparison = currentNode.compare(p);
			if (comparison > 0) {
				currentNode = currentNode.rightChild();
			}
			else if (comparison < 0) {
				currentNode = currentNode.leftChild();
			}
			else {
				if (currentNode.point().equals(p))
					return true;
				currentNode = currentNode.rightChild();
			}
		}
		return false;
	}

	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) throw new NullPointerException();
		java.util.Stack<Point2D> stack = new java.util.Stack<Point2D>();
		rangeRecursive(rootNode, stack, rect);
		return stack;
	}
	
	private static void rangeRecursive(Node currentNode, java.util.Stack<Point2D> stack, RectHV rect) {
		if (currentNode == null) return;
		if (!currentNode.rect().intersects(rect)) return;
		if (rect.contains(currentNode.point())) 
			stack.push(currentNode.point());
		rangeRecursive(currentNode.rightChild(), stack, rect);
		rangeRecursive(currentNode.leftChild(), stack, rect);
	}
	
	public Point2D nearest(Point2D p) {
		if (p == null) throw new NullPointerException();
		if (rootNode == null) return null;
		return nearestRecursive(rootNode, p, rootNode.point());
		// a nearest neighbor in the set to p; null if set is empty
	}
	
	private Point2D nearestRecursive(Node currentNode, Point2D p, Point2D nearest) {
		//currentNode will not be null.
		
		// when current point is closer, replace nearest with it.
		if (currentNode.point().distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
			nearest = currentNode.point();
		}
		
		// when both child are not null, check the closer child first to get better pruning
		if (currentNode.rightChild() != null && currentNode.leftChild() != null) {
			Point2D subtree = nearest;
			
			// when p is on the right/upper side of currentNode line
			if (currentNode.compare(p) >= 0) {
				//check right subtree first, then check left subtree
				if (currentNode.rightChild().rect().distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
					subtree = nearestRecursive(currentNode.rightChild(), p, nearest);
					if (subtree != nearest) {
						nearest = subtree;
					}
				}
				if (currentNode.leftChild().rect().distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
					subtree = nearestRecursive(currentNode.leftChild(), p, nearest);
					if (subtree != nearest) {
						nearest = subtree;
					}
				}
			}
			else { // when p is on the left/lower side of currentNode line
				//check left subtree first, then check right subtree
				if (currentNode.leftChild().rect().distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
					subtree = nearestRecursive(currentNode.leftChild(), p, nearest);
					if (subtree != nearest) {
						nearest = subtree;
					}
				}
				if (currentNode.rightChild().rect().distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
					subtree = nearestRecursive(currentNode.rightChild(), p, nearest);
					if (subtree != nearest) {
						nearest = subtree;
					}
				}
			}
		}
		
		else if (currentNode.rightChild() != null) {
			if (currentNode.rightChild().rect().distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
				Point2D rightSubtree = nearestRecursive(currentNode.rightChild(), p, nearest);
				if (rightSubtree != nearest) {
					nearest = rightSubtree;
				}
			}
		}
		
		
		else if (currentNode.leftChild() != null) {
			if (currentNode.leftChild().rect().distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
				Point2D leftSubtree = nearestRecursive(currentNode.leftChild(), p, nearest);
				if (leftSubtree != nearest) {
					nearest = leftSubtree;
				}
			}
		}
		
		return nearest;
	}
}