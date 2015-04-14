import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private Item[] items;
	private int size;
	private int tail;
	private int head;

	@SuppressWarnings("unchecked")
	public RandomizedQueue() {
		// construct an empty randomized queue
		items = (Item[]) new Object[2];
		size = 0;
		head = 1;
		tail = 0;
	}

	public boolean isEmpty() {
		// is the queue empty?
		return size <= 0;
	}

	public int size() {
		// return the number of items on the queue
		return size;
	}

	public void enqueue(Item item) {
		// add the item
		if (item == null) {
			throw new NullPointerException("Can't add null item!");
		}
		if (tail >= items.length) {
			resize(items.length * 2);
		}
		items[tail++] = item;
		size++;
	}

	public Item dequeue() {
		// delete and return a random item
		if (isEmpty()) {
			throw new NoSuchElementException("Queue is empty!");
		}
		int randomIndex = StdRandom.uniform(head - 1, tail);
		while (items[randomIndex] == null) {
			randomIndex = StdRandom.uniform(head - 1, tail);
		}
		Item item = items[randomIndex];
		items[randomIndex] = null;
		if (randomIndex == tail) {
			tail--;
		}
		if (randomIndex == head - 1) {
			head++;
			if (head == 0) {
				head = 1;
			}
		}
		size--;
		// shrink size of array if necessary
		if (size == items.length / 4 && items.length > 4) {
			resize(items.length / 2);
		}
		return item;
	}

	public Item sample() {
		// return (but do not delete) a random item
		if (isEmpty()) {
			throw new NoSuchElementException("Queue is empty!");
		}
		int randomIndex = StdRandom.uniform(head - 1, tail);
		while (items[randomIndex] == null) {
			randomIndex = StdRandom.uniform(head - 1, tail);
		}
		return items[randomIndex];
	}

	public Iterator<Item> iterator() {
		// return an independent iterator over items in random order
		return new RandomArrayIterator();
	}

	private class RandomArrayIterator implements Iterator<Item> {

		private int[] usedRandomIndexes;
		private int elementsAccessed;

		public RandomArrayIterator() {
			usedRandomIndexes = new int[size];
			elementsAccessed = 0;
			for (int i = 0; i < usedRandomIndexes.length; i++) {
				usedRandomIndexes[i] = -1;
			}
		}

		public boolean hasNext() {
			return elementsAccessed < size;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			int randomIndex = StdRandom.uniform(head - 1, tail);
			while (items[randomIndex] == null || indexUsed(randomIndex)) {
				randomIndex = StdRandom.uniform(head - 1, tail);
			}
			usedRandomIndexes[elementsAccessed++] = randomIndex;
			return items[randomIndex];
		}

		private boolean indexUsed(int index) {
			for (int usedIndex : usedRandomIndexes) {
				if (index == usedIndex) {
					return true;
				}
			}
			return false;
		}
	}

	// resize the underlying array holding the elements
	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		Item[] temp = (Item[]) new Object[capacity];
		int j = 0;
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
			if (item != null) {
				temp[j] = item;
				j++;
			}
		}
		items = temp;
		head = 1;
		tail = j;
	}
}
