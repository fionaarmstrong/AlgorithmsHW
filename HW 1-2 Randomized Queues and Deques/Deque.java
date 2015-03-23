
public class Deque<Item> implements Iterable<Item> {
	private int size;
	private Node head;
	private Node tail;
	
	private class Node {
		private Node prev;
		private Node next;
		private Item item;
		public Node(Item inputItem) {
			item = inputItem;
		}
		
		public Node getPrev() {
			return prev;
		}
		
		public void setPrev(Node input) {
			prev = input;
		}
		
		public Node getNext() {
			return next;
		}
		
		public void setNext(Node input) {
			next = input;
		}
		
		public void setItem(Item inputItem) {
			item = inputItem;
		}
		
		public Item getItem() {
			return item;
		}
	}
	
	public Deque() {
		head = new Node(null);
		tail = new Node(null);
		head.setNext(new Node(null));
		head.getNext().setPrev(head);
		head.getNext().setNext(tail);
		tail.setPrev(head.getNext());
		size = 0;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public int size() {
	return size;
	}
	
	public void addFirst(Item item) {
	if (item == null)
	throw new java.lang.NullPointerException();
	
	else if (size == 0) {
		head.getNext().setItem(item);
	}
	
	else {
		Node newItem = new Node(item);
		head.getNext().setPrev(newItem);
		newItem.setNext(head.getNext());
		newItem.setPrev(head);
		head.setNext(newItem);
	}
	size++;
	}
	
	public void addLast(Item item) {
		if (item == null)
			throw new java.lang.NullPointerException();
		else if (size == 0) {
			head.getNext().setItem(item);
			}
			
		else {
			Node newItem = new Node(item);
			tail.getPrev().setNext(newItem);
			newItem.setNext(tail);
			newItem.setPrev(tail.getPrev());
			tail.setPrev(newItem);
			}
		size++;
	}
	
	public Item removeFirst() {
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		Node returnNode = head.getNext();
		if (size != 1) {
			returnNode.getNext().setPrev(head);
			head.setNext(returnNode.getNext());
			returnNode.setNext(null);
			returnNode.setPrev(null);
		}
		size--;
		return returnNode.getItem();
	}
	
	public Item removeLast() {
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		Node returnNode = tail.getPrev();
		if (size != 1) {
			returnNode.getPrev().setNext(tail);
			tail.setPrev(returnNode.getPrev());
			returnNode.setNext(null);
			returnNode.setPrev(null);
		}
		size--;
		return returnNode.getItem();
	}
	
	
	public java.util.Iterator<Item> iterator() {
	return new Iterator();
	}
	
	private class Iterator implements java.util.Iterator<Item> {
		private Node curr;
		
		public Iterator() {
			curr = head.getNext();
		}
	
		public boolean hasNext() {
			return curr != tail && size != 0;
		}
	
		public Item next() {
			if (hasNext()) {
				Item returnItem = curr.getItem();
				curr = curr.next;
		     	return returnItem;
			}
			else throw new java.util.NoSuchElementException();
		}
	
		@Override
		public void remove() {
				throw new java.lang.UnsupportedOperationException();
		}
	}
	
	/*
	public static void main(String[] args){
	Deque<Integer> queue = new Deque<Integer>();
	java.util.Iterator<Integer> it = queue.iterator();
	for (int i = 0; i<2000; i++) {
		queue.addLast(1);
		queue.removeFirst();
	}
	while (it.hasNext()) {
		System.out.println(it.next());
	}
	//queue.removeFirst();
	}*/
}
		
	