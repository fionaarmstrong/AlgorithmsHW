public class RandomizedQueue<Item> implements Iterable<Item> {
	private int size;
	private Node head;
	private Node tail;
	
	private class Node {
		public Node prev;
		public Node next;
		private Item item;
		public Node(Item item) {
			this.item = item;
		}
		
		public void setItem(Item item) {
			this.item = item;
		}
		
		public Item getItem() {
			return item;
		}
	}
	
	public RandomizedQueue() {
		head = new Node(null);
		tail = new Node(null);
		head.next = new Node(null);
		head.next.prev = head;
		head.next.next = tail;
		tail.prev = head.next;
		size = 0;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public int size() {
		return size;
	}
		
	public void enqueue(Item item) {
		if (item == null) throw new java.lang.NullPointerException();
		else if (size == 0) {
			head.next.setItem(item);
		}
		
		else {
			Node newItem = new Node(item);
			head.next.prev = newItem;
			newItem.next = head.next;
			newItem.prev = head;
			head.next = newItem;
		}
		size++;
	}
	
	private Node getRandomNode() {
		StdRandom.setSeed(StdRandom.uniform(1234567890));
		int randomNumber = StdRandom.uniform(size);
		Node currNode = null;
		if (randomNumber < size()/2) {
			currNode = head.next;
			while (randomNumber > 0) {
				currNode = currNode.next;
				randomNumber--;
			}
		}
		else if (randomNumber >= size()/2) {
			currNode = tail.prev;
			while (randomNumber < size()-1) {
				currNode = currNode.prev;
				randomNumber++;
			}
		}
		return currNode;
	}
	
	public Item dequeue() {
		if (isEmpty()) throw new java.util.NoSuchElementException();
		
		Node returnNode ;
		if (size == 1) returnNode = head.next;
		else {
			returnNode = getRandomNode();
			returnNode.prev.next = returnNode.next;
			returnNode.next.prev = returnNode.prev;
			returnNode.next = null;
			returnNode.prev = null;
		}
		size--;
		return returnNode.getItem();
	}
	
	public Item sample() {
		if (isEmpty()) throw new java.util.NoSuchElementException();
		if (size == 1) return head.next.getItem();
		return getRandomNode().getItem();
	}
	
	/*private void printAll() {
		Node tracker = head.next;
		while(tracker != tail) {
			System.out.println(tracker.getItem());
			tracker = tracker.next;
		}
	}*/
	
	public java.util.Iterator<Item> iterator() {
		return new Iterator();
	}
	
	private class Iterator implements java.util.Iterator<Item> {
		private Node currNode;
		private boolean[] served;

		public Iterator() {
			currNode = head.next;
			served = new boolean[size()];
		}

		@Override
		public boolean hasNext() {
			for (boolean x: served) 
				if (!x)
					return true;
			return false;
		}

		@Override
		public Item next() {
		if (hasNext()) {
			int randomNumber;
			do {
				randomNumber = StdRandom.uniform(served.length);
			} while (served[randomNumber]);
			
			served[randomNumber] = true;
			
			if (randomNumber < served.length/2) {
				currNode = head.next;
				while (randomNumber > 0) {
					currNode = currNode.next;
					randomNumber--;
				}
			}
			else if (randomNumber >= served.length/2) {
				currNode = tail.prev;
				while (randomNumber < served.length-1) {
					currNode = currNode.prev;
					randomNumber++;
				}
			}
			
			return currNode.getItem();
		}
		else throw new java.util.NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}

		}
	
	/*
	private class Iterator implements java.util.Iterator<Item> {
		private Node curr;
		private int[] seq;
		private int index;

		public Iterator() {
			index = 0;
			curr = head.next;
			seq = new int[size()];
			for (int i = 0; i<size; i++){
				seq[i] = i;
				
			}
			StdRandom.shuffle(seq);
			/*for (int i = 0; i < size; i++)
				System.out.println("seq["+i+"] is "+seq[i]);
			
			int location = seq[0];
			while (location != 0) {
				curr = curr.next;
				location--;
			}
		}

		@Override
		public boolean hasNext() {
			if (index == size()) seq = null;
			return index < size();
		}

		@Override
		public Item next() {
		if (hasNext()) {
			Item returnItem = curr.getItem();
			if (index != size()-1) {
				int steps = seq[index+1] - seq[index];
				if (steps > 0) {
					while (steps >0) {
						curr = curr.next;
						steps--;
					}
				}
				else if (steps < 0) {
					while (steps < 0) {
						curr = curr.prev;
						steps++;
					}
				}
			}
			index++;
			if (index == size()) seq = null;
			return returnItem;
		}
		else throw new java.util.NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}

		}
	*/
	
	

public static void main(String[] args){
		RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
		queue.enqueue(0);
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);
		queue.enqueue(4);
		
		java.util.Iterator<Integer> it = queue.iterator();
		//System.out.println("Size:"+queue.size()+" "+queue.isEmpty());
		while(it.hasNext()){
			System.out.println("NEXT:"+it.next());
		}
		it = queue.iterator();
		System.out.println("Another Iterator");
		while(it.hasNext()){
			System.out.println("NEXT:"+it.next());
		}
		
	}
	
}