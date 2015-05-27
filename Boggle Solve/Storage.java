import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/** A container for storing elements of type T in one of several
 *  possible underlying data structures.
 *  Additional data structures (or variations on data structures)
 *  can be added by adding to the DataStructure enum values and
 *  adding corresponding cases to wrapper methods.
 *  
 *  @author mvail
 */
public class Storage<T> {
	/** supported underlying data structures for Storage to use */
	public static enum DataStructure {stack, queue}
	/** the data structure chosen for this Storage to use */
	private DataStructure dataStructure;
	/** the data structures - only one will be instantiated and used by this Storage */
	private Queue<T> queue;
	private Stack<T> stack;
	
	/** Constructor
	 * @param dataStructure choice of DataStructures 
	 */
	public Storage(DataStructure dataStructure) {
		this.dataStructure = dataStructure;
		switch (this.dataStructure) {
		case stack:
			stack = new Stack<T>();
			break;
		case queue:
			queue = new LinkedList<T>();
		}
	}
	
	/** Add element to underlying data structure
	 * @param element T to store
	 */
	public void store(T element) {
		switch (this.dataStructure){
		case stack:
			stack.push(element);
			break;
		case queue:
			queue.offer(element);
		}
	}
	
	/** Remove and return the next T from storage
	 * @return next T from storage
	 */
	public T retrieve() {
		switch (this.dataStructure){
		case stack:
			return stack.pop();
		case queue:
			return queue.poll();
		default:
			throw new RuntimeException("unreachable"); //shouldn't ever happen b/c we're using an enum and have all types covered
		}
	}
	
	/** @return true if store is empty, else false */
	public boolean isEmpty() {
		switch (this.dataStructure){
		case stack:
			return stack.isEmpty();
		case queue:
			return queue.isEmpty();
		default:
			throw new RuntimeException("unreachable"); //shouldn't ever happen b/c we're using an enum and have all types covered
		}
	}
	
	/** @return size of store */
	public int size() {
		switch (this.dataStructure){
		case stack:
			return stack.size();
		case queue:
			return queue.size();
		default:
			throw new RuntimeException("unreachable"); //shouldn't ever happen b/c we're using an enum and have all types covered
		}
	}
} // class Storage