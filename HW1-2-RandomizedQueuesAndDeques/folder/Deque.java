import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

	private class Node {
		public Node(Item item) {
			this.item = item;
		}

		private Item item;
		private Node next;
		private Node prev;
	}

	private int size;
	private Node header;

	public Deque() {
		// construct an empty deque
		header = new Node(null);
		header.next = header;
		header.prev = header;
		size = 0;
	}

	public boolean isEmpty() {
		// is the deque empty?
		return size <= 0;
	}

	public int size() {
		return size;
	}

	public void addFirst(Item item) {
		// insert the item at the front
		if (item == null) {
			throw new NullPointerException("Can't add null item!");
		}
		Node newNode = new Node(item);
		newNode.prev = header.next;
		newNode.next = header;
		header.next.next = newNode;
		header.next = newNode;
		if (header.prev == header) {
			header.prev = newNode;
		}
		size++;
	}

	public void addLast(Item item) {
		// insert the item at the end
		if (item == null) {
			throw new NullPointerException("Can't add null item!");
		}
		Node newNode = new Node(item);
		newNode.next = header.prev;
		newNode.prev = header;
		header.prev.prev = newNode;
		header.prev = newNode;
		if (header.next == header) {
			header.next = newNode;
		}
		size++;
	}

	public Item removeFirst() {
		// delete and return the item at the front
		if (isEmpty()) {
			throw new NoSuchElementException("Deque is empty!");
		}
		Item item = header.next.item;
		header.next.item = null;
		if (header.next.prev == header) {
			header.next = header;
			header.prev = header;
		} else {
			header.next.prev.next = header;
			header.next = header.next.prev;
		}
		size--;
		return item;
	}

	public Item removeLast() {
		// delete and return the item at the end
		if (isEmpty()) {
			throw new NoSuchElementException("Deque is empty!");
		}
		Item item = header.prev.item;
		header.prev.item = null;
		if (header.prev.next == header) {
			header.prev = header;
			header.next = header;
		} else {
			header.prev.next.prev = header;
			header.prev = header.prev.next;
		}
		size--;
		return item;
	}

	public Iterator<Item> iterator() {
		// return an iterator over items in order from front to end
		return new DequeIterator();
	}

	private class DequeIterator implements Iterator<Item> {

		private Node current = header.next;

		public boolean hasNext() {
			return current != header;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Item item = current.item;
			current = current.prev;
			return item;
		}
	}

	public static void main(String[] args) {
		Deque<String> deque = new Deque<String>();
		deque.addFirst("1");
		deque.addFirst("2");
		StdOut.println(deque.removeLast());
		deque.addFirst("3");
		StdOut.println(deque.removeLast());
		StdOut.println(deque.removeLast());
		deque.addFirst("4");
		StdOut.println(deque.removeLast());
	}
}
