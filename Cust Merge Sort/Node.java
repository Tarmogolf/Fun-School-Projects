
public class Node<T> {

	private T data; //the data contained in a given node
	private Node<T> next, prev; //the next and previous nodes in a given list.
	
	public Node(){
		data = null;
		next = null;
		prev = null;
	}

	/**
	 * Accessor method for the data contained in a node.
	 * 
	 * @return the data contained in a given node.
	 */
	public T getData() {
		return data;
	}

	/**
	 * Mutator method for the data contained in a node.
	 * 
	 * @param data the data of type T that you want to set.
	 */
	public void setData(T data) {
		this.data = data;
	}
	
	/**
	 * Accessor method for the following node in a list.
	 * 
	 * @return the node following the current node
	 */
	public Node<T> getNext() {
		return next;
	}
	
	/**
	 * Mutator method for the following node in a list.
	 * 
	 * @param next the node that should follow the current node
	 */
	public void setNext(Node<T> next) {
		this.next = next;
	}
	
	/**
	 * Accessor method for the trailing node in a list.
	 * 
	 * @return the node behind the current node
	 */
	public Node<T> getPrev() {
		return prev;
	}
	
	/**
	 * Mutator method for the trailing node in a list.
	 * 
	 * @param prev the node that should be behind a specified node
	 */
	public void setPrev(Node<T> prev) {
		this.prev = prev;
	}
}
