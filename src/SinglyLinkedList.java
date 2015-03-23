
public class SinglyLinkedList<E> {
	public Node head;
	
	public class Node {
		private E data;
		public Node next;
		
		public Node(E e) {
			data = e;
		}
		
		public E getData() {
			return data;
		}
	}
	
	public SinglyLinkedList() {
	}
	
	public SinglyLinkedList(E first) {
		head = new Node(first);
	}
	
	public void add(E e) {
		Node curr = head;
		while (curr.next!=null) {
			curr = curr.next;
		}
		curr.next = new Node(e);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SinglyLinkedList<Integer> list = new SinglyLinkedList(2);
		list.add(3);
		System.out.print(list.head.getData());
		
	}

}
