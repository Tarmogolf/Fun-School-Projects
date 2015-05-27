import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A custom implementation of the methods defined in the
 * DoubleLinkedListADT<T>. Provides the user with a list
 * for storing elements of any type T.
 * 
 * @author Stan Bessey
 *
 */
public class DoubleLinkedList<T> implements DoubleLinkedListADT<T>{

	private int size, modCount; //keeps track of the size and number of times the list has been modified, respectively
	private Node<T> head, tail; //first and last elements in the list
	private ElementNotFoundException msg = new ElementNotFoundException("DoubleLinkedList");
	private EmptyCollectionException emptyMsg = new EmptyCollectionException("DoubleLinkedList");

	/**
	 * Creates a new DoubleLinkedList with no nodes, and thus no size.
	 */
	public DoubleLinkedList(){
		size = 0;
		modCount = 0;
		head = tail = null;
	}

	@Override
	/**  
	 * Adds the specified element to the front of this list. 
	 *
	 * @param element the element to be added to the front of this list    
	 */
	public void addToFront(T element) {
		Node<T> newNode = new Node<T>();//temp node to be added
		newNode.setData(element);

		//head will only be null if the list is empty
		//so the new element would be the only element in the list
		//which means it would be the head and tail simultaneously.
		if(head == null){
			head = newNode;
			tail = newNode;
		}else{
			newNode.setNext(head);
			head.setPrev(newNode);
			head = newNode;
			head.setPrev(null);
		}

		size++;
		modCount++;
	}

	public void haveFun(){
		
	}
	@Override
	/**  
	 * Adds the specified element to the rear of this list. 
	 *
	 * @param element the element to be added to the rear of this list    
	 */
	public void addToRear(T element) {
		Node<T> newNode = new Node<T>();//temp node to be added to end of list
		newNode.setData(element);

		//tail will only be null if the list is empty
		//so the new element would be the only element in the list
		//which means it would be the head and tail simultaneously.
		if(tail == null){
			tail = newNode;
			head = newNode;
		}else{
			tail.setNext(newNode);
			newNode.setPrev(tail);
			tail = newNode;
			tail.setNext(null);
		}	

		size++;
		modCount++;
	}

	@Override
	/**  
	 * Adds the specified element after the specified target. 
	 *
	 * @param element the element to be added after the target
	 * @param target  the target is the item that the element will be added after
	 * @throws ElementNotFoundException thrown if the "target" parameter is not found
	 * @throws EmptyCollectionException thrown if the list is currently empty
	 */
	public void addAfter(T element, T target) throws EmptyCollectionException, ElementNotFoundException {
		if(size == 0){
			throw emptyMsg;
		}
		
		Node<T> cursor = head;//used to move one at a time through the list
		Node<T> newNode = new Node<T>();//temp node to be added
		newNode.setData(element);

		//step through each node in the list, starting at the head to check 
		//where the target element is located
		while(!(cursor.getData().equals(target))){
			if(cursor.getNext() == null){//only null if at end of list, which means matching target not found
				throw msg;
			}
			cursor = cursor.getNext();
		}

		newNode.setNext(cursor.getNext());
		newNode.setPrev(cursor);

		//rearranges the nodes already in the list to point at the proper objects.
		//the first conditional makes sure we're not at the last index of the list.
		if(cursor.getNext() != null){
			cursor.getNext().setPrev(newNode);
		}else{
			tail = newNode;
		}
		cursor.setNext(newNode);

		size++;
		modCount++;
	}

	@Override
	/**  
	 * Removes and returns the first element from this list. 
	 * 
	 * @return the first element from this list
	 * @throws EmptyCollectionException Cannot remove an element from an empty collection
	 */
	public T removeFirst() throws EmptyCollectionException{
		if(size == 0){
			throw emptyMsg;
		}

		Node<T> removedNode = head;

		if(size == 1){// removing the only element from a list, can just set head and tail to null
			head = tail = null;
		}else{
			head = head.getNext();
			head.setPrev(null);
		}	

		size--;
		modCount++;

		return removedNode.getData();
	}

	@Override
	/**  
	 * Removes and returns the last element from this list. 
	 *
	 * @return the last element from this list
	 * @throws EmptyCollectionException
	 */
	public T removeLast() throws EmptyCollectionException{
		if(size == 0){
			throw emptyMsg;
		}

		Node<T> removedNode = tail;

		if(size == 1){// removing the only element from a list, can just set head and tail to null
			head = tail = null;
		}else{
			tail = tail.getPrev();
			tail.setNext(null);
		}	

		size--;
		modCount++;

		return removedNode.getData();
	}

	@Override
	/**  
	 * Removes and returns the specified element from this list by walking
	 * through the list, checking each node's data against the desired
	 * element. Can invoke removeFirst() or removeLast() if the element
	 * is found in the first or last node, for simplicity's sake.
	 *
	 * @param element the element to be removed from the list
	 * @throws ElementNotFoundException thrown once we reach the end of the list and still
	 * 									haven't found the desired element.
	 * @throws EmptyCollectionException cannot remove an item from an empty collection.
	 */
	public T remove(T element) throws ElementNotFoundException, EmptyCollectionException {
		if(size == 0){
			throw emptyMsg;
		}

		Node<T> cursor = head;//used to step through each element in the list

		while(!cursor.getData().equals(element)){
			if(cursor.getNext() == null){
				throw msg;//thrown if we are at the last node in the list and still haven't found element
			}
			cursor = cursor.getNext();//move down one node
		}

		Node<T> removedNode;

		if(cursor == head){
			removedNode = head;
			this.removeFirst();
		}else if(cursor == tail){
			removedNode = tail;
			this.removeLast();
		}else{
			removedNode = cursor;
			cursor.getPrev().setNext(cursor.getNext());
			cursor.getNext().setPrev(cursor.getPrev());

			//size and modCount are modified here since removeFirst() 
			//and removeLast() already modify the variables.
			size--; 
			modCount++;
		}

		return removedNode.getData();
	}

	@Override
	/**  
	 * Returns a reference to the first element in this list. 
	 *
	 * @return a reference to the first element in this list
	 * @throws EmptyCollectionException an empty collection does not have a "first" element
	 */
	public T first() throws EmptyCollectionException{
		if(size == 0){
			throw emptyMsg;
		}
		return head.getData();
	}

	@Override
	/**  
	 * Returns a reference to the last element in this list. 
	 *
	 * @return a reference to the last element in this list
	 * @throws EmptyCollectionException an empty collection does not have a "first" element
	 */
	public T last() throws EmptyCollectionException{
		if(size == 0){
			throw emptyMsg;
		}
		return tail.getData();
	}

	@Override
	/**  
	 * Returns true if this list contains the specified target element by stepping
	 * through each node and comparing its data value against the target data
	 * value.
	 *
	 * @param target the target that is being sought in the list
	 * @return true if the list contains this element
	 */
	public boolean contains(T target) {
		boolean containsTarget = false;

		if(size == 0){
			return containsTarget;
		}

		Node<T> cursor = head;

		// Steps through the list node by node until either target is found or
		// we reach the end of the list. If we reach the end of the list and haven't
		//found the target, then we can return false. If we ever break out of the
		//while loop without returning false, then containsTarget must be true.

		while(!(cursor.getData().equals(target))){
			if(cursor.getNext() == null){
				return containsTarget;
			}
			cursor = cursor.getNext();
		}
		containsTarget = true;

		return containsTarget;
	}

	@Override
	/**  
	 * Returns true if this list contains no elements. 
	 *
	 * @return true if this list contains no elements
	 */
	public boolean isEmpty() {

		return size == 0;
	}

	@Override
	/**  
	 * Returns the number of elements in this list. 
	 *
	 * @return the integer representation of number of elements in this list
	 */
	public int size() {
		return size;
	}

	@Override
	/**
	 * DoubleListIterator implements ListIterator, which in turn implements
	 * Iterator. By specifying that the iterator() method returns an
	 * Iterator, and not a ListIterator, then there is no need to create a 
	 * separate iterator class, since this method will return an iterator
	 * with access to only the next(), hasNext(), and remove() methods.
	 */
	public Iterator<T> iterator() {
		return new DoubleListIterator();
	}

	@Override
	/**  
	 * Inserts the specified element at the specified index. We can take 
	 * advantage of the addtoFront(element) and addtoRear(element) methods
	 * if size is 0 or size, respectively.
	 * 
	 * @param index   the index into the array to which the element is to be
	 *                inserted.
	 * @param element the element to be inserted into the array
	 * @throws IndexOutOfBoundsException  thrown if trying to insert an element
	 * 									  outside of the boundaries of the list.
	 */
	public void add(int index, T element) throws IndexOutOfBoundsException {
		if(index > size || index < 0){
			throw new IndexOutOfBoundsException("index cannot be larger than the current size of the list, or less than zero.");
		}

		int counter = 0;
		Node<T> newNode = new Node<T>();//temp node to be added
		newNode.setData(element);
		Node<T> cursor = head; 

		//steps through each node until we reach the right index
		while(counter != index){
			cursor = cursor.getNext();
			counter++;
		}

		if(index == 0){
			this.addToFront(element);
		}else if (index == size){
			this.addToRear(element);
		}else{
			newNode.setNext(cursor);
			newNode.setPrev(cursor.getPrev());
			cursor.getPrev().setNext(newNode);
			cursor.setPrev(newNode);
			size++;	
			modCount++;
		}
	}


	@Override
	/**  
	 * Sets the element at the specified index by walking through each element
	 * of the list using a counter.
	 *
	 * @param index   the index into the array to which the element is to be set
	 * @param element the element to be set into the list
	 * @throws IndexOutOfBoundsException
	 */
	public void set(int index, T element) {
		if(index >= size || index < 0){
			throw new IndexOutOfBoundsException("index cannot be larger than or equal to the current size of the list, or less than zero.");
		}

		int counter = 0;
		Node<T> cursor = head;
		while(counter != index){//steps through each node until we get to the desired index
			cursor = cursor.getNext();
			counter++;
		}

		cursor.setData(element);
		modCount++;		
	}

	@Override
	/**  
	 * Adds the specified element to the rear of this list. Is functionally
	 * identical to the addToRear() method.
	 *
	 * @param element  the element to be added to the rear of the list    
	 */
	public void add(T element) {
		this.addToRear(element);
	}


	@Override
	/**  
	 * Returns a reference to the element at the specified index. 
	 *
	 * @param index  the index to which the reference is to be retrieved from
	 * @return the element at the specified index  
	 * @throws IndexOutOfBoundsException thrown if the desired index is outside of the list
	 */
	public T get(int index) {
		if(index >= size || index < 0){
			throw new IndexOutOfBoundsException("index cannot be larger than or equal to the current size of the list, or less than zero.");
		}

		Node<T> cursor = head;
		int counter = 0;
		while(counter != index){//step through each node until we get to the proper index
			cursor = cursor.getNext();
			counter++;
		}

		return cursor.getData();
	}

	@Override
	/**  
	 * Returns the index of the specified element. 
	 *
	 * @param element  the element for the index is to be retrieved
	 * @return the integer index for this element  
	 * @throws ElementNotFoundException thrown if the desired element is not found in the list
	 */
	public int indexOf(T element) throws ElementNotFoundException{

		if(size == 0){
			throw msg;//won't find anything on an empty list
		}

		int counter = 0;
		Node<T> cursor = head;

		//step through list until we find the target element.
		while(!(cursor.getData().equals(element))){
			if(cursor.getNext() == null){
				throw msg;
			}

			cursor = cursor.getNext();
			counter++;
		}

		return counter;
	}

	@Override
	/**  
	 * Returns and removes the element at the specified index. We can
	 * take advantage of the removeFirst() and removeLast() methods if 
	 * the cursor stops on the head or tail of the list.
	 *
	 * @param index the index of the element to be retrieved and removed
	 * @return the element at the given index
	 * @throws IndexOutOfBoundsException thrown if the desired index is outside of the list 
	 */
	public T remove(int index) throws IndexOutOfBoundsException{
		if(index >= size || index < 0){
			throw new IndexOutOfBoundsException("index cannot be larger than or equal to the current size of the list, or less than zero.");
		}

		int counter = 0;
		Node<T> cursor = head;

		//step through the list until we find the right index
		while(counter != index){
			cursor = cursor.getNext();
			counter++;
		}

		if(cursor == head){
			this.removeFirst();
		}else if (cursor == tail){
			this.removeLast();
		}else{
			cursor.getPrev().setNext(cursor.getNext());
			cursor.getNext().setPrev(cursor.getPrev());
			size--;
			modCount++;
		}

		return cursor.getData();
	}
	
	@Override
	/**
     * Returns a portion of the current DoubleLinkedList as a new,
     * independent DoubleLinkedList by iterating through each element
     * in the list, from beginning(inclusive) to end (exclusive)
     * 
     * @param beginning the index at which to begin the new sublist
     * @param end the index at which to end the new sublist - can go one element "off" of the list, since end is exclusive
     * @return A partial list of the original list from idnex beginning to index end, exclusive.
     */
	public DoubleLinkedList<T> subList(int beginning, int end){
		DoubleLinkedList<T> newList = new DoubleLinkedList<T>();
		
		int counter = beginning;
		ListIterator<T> myIt = listIterator(beginning); 
		
		while(counter < end){
			newList.add(myIt.next());
			counter++;
		}
		
		return newList;		
	}

	@Override
	/**
	 * Returns a more robust version of the iterator() method
	 * that has the full functionality of the ListIterator 
	 * class. Begins at the default starting position, the 
	 * beginning of the list.
	 */
	public ListIterator<T> listIterator() {
		return new DoubleListIterator();
	}

	@Override
	/**
	 * Returns a more robust version of the iterator() method
	 * that has the full functionality of the ListIterator 
	 * class. Starts at a user specified index.
	 * 
	 * @param startingIndex the index at which to initially point
	 * 						the iterator
	 */
	public ListIterator<T> listIterator(int startingIndex) {
		return new DoubleListIterator(startingIndex);
	}

	/**
	 * A string representation of the DoubleLinkedList.
	 */
	public String toString(){
		String myString = "";

		if(size == 0){
			myString = "The list is empty.";
		}else{
			Node<T> cursor = head;
			int counter = 0;
			while(counter != size){
				myString += cursor.getData(); 
				if(counter < size-1){
					myString += ", ";
				}
				counter++;
				cursor = cursor.getNext();
			}
		}
		return myString;
	}
	
	/**
	 * Private inner class that constructs an implementation of a
	 * ListIterator. Can be called by the user via the
	 * listIterator() and listIterator(int) methods. Can also return
	 * a more simple one-direction iterator using the
	 * iterator() method.
	 * 
	 * @author Stan Bessey
	 *
	 */
	private class DoubleListIterator implements ListIterator<T>{

		private int iteratorModCount;//tracks number of modifications made to the list by the iterator
		private int current; //current index pointer of the iterator
		private int stateTracker; //used to validate if set/remove can be called

		/**
		 * Sets up this iterator using the modCount of DoubleLinkedList
		 * and a default starting point at the beginning of the list. 
		 */
		public DoubleListIterator(){
			iteratorModCount = modCount;
			current = -1;
			stateTracker = 0;
		}

		/**
		 * Sets up this iterator using the modCount of DoubleLinkedList
		 * and a user defined starting point in the list of "index".
		 * 
		 *  @param index the starting position of the iterator in the list
		 */
		public DoubleListIterator(int index){
			iteratorModCount = modCount;
			current = index-1;
			stateTracker = 0;
		}

		@Override
		/**
		 * Returns true if this iterator has at least one more element
		 * to deliver in the iteration.
		 *
		 * @return  true if this iterator has at least one more element to deliver
		 *          in the iteration
		 * @throws  ConcurrentModificationException if the collection has changed
		 *          while the iterator is in use
		 */
		public boolean hasNext() throws ConcurrentModificationException{
			if (iteratorModCount != modCount){
				throw new ConcurrentModificationException();
			}

			return current < size-1;
		}

		@Override
		/**
		 * Returns the next element in the iteration. If there are no
		 * more elements in this iteration, a NoSuchElementException is
		 * thrown.
		 *
		 * @return  the next element in the iteration
		 * @throws  NoSuchElementException if an element not found exception occurs
		 * @throws  ConcurrentModificationException if the collection has changed
		 */
		public T next() throws ConcurrentModificationException, NoSuchElementException{
			if (!this.hasNext()){
				throw new NoSuchElementException();
			}
			if (iteratorModCount != modCount){
				throw new ConcurrentModificationException();
			}
			current++;
			stateTracker++;

			return DoubleLinkedList.this.get(current);
		}

		@Override
		/**
		 * Removes the element that the iterator is currently
		 * pointing to.
		 * 
		 * @throws IllegalStateException thrown if previous/next have not been called,
		 * 								 or if remove/add have been called without calling
		 * 								 previous/next afterwards.
		 */
		public void remove() throws IllegalStateException{
			if(stateTracker <= 0){
				throw new IllegalStateException("neither next nor previous have been called, or remove or add have been called after the last call to next or previous");
			}
			
			DoubleLinkedList.this.remove(current);
			iteratorModCount++;
			stateTracker = 0;
		}

		@Override
		/**
		 * Adds the specified element at the index that the
		 * iterator is currently pointing to. Resets stateTracker
		 * to 0.
		 */
		public void add(T element) {
			current++;
			if(current  == -1){
				DoubleLinkedList.this.add(element);
				current++; //sets cursor properly if there is only one element in the list.
			}else{
				DoubleLinkedList.this.add(current, element);
			}
			iteratorModCount++;
			stateTracker = 0;
		}

		@Override
		/**
		 * Returns true if this iterator has at least one more element
		 * to deliver in the iteration while traversing the list in
		 * the reverse direction. Alternating calls between next and previous
		 * should return the same element.
		 *
		 * @return  true if this iterator has at least one more element to deliver
		 *          in the iteration, traveling in reverse order of the list
		 * @throws  ConcurrentModificationException if the collection has changed
		 *          while the iterator is in use
		 */
		public boolean hasPrevious() throws ConcurrentModificationException{
			if (iteratorModCount != modCount){
				throw new ConcurrentModificationException();
			}

			return current >= 0;
		}

		@Override
		/**
		 * Returns the index value of the next item in the list that
		 * the iterator is currently pointing at.
		 * 
		 * @return the index of the next item in the list
		 */
		public int nextIndex() {

			return current+1;
		}

		@Override
		/**
		 * Returns the previous element in the iteration. If there are no
		 * more elements in this iteration, a NoSuchElementException is
		 * thrown.
		 *
		 * @return  the next element in the iteration
		 * @throws  NoSuchElementException if an element not found exception occurs
		 * @throws  ConcurrentModificationException if the collection has changed
		 */
		public T previous() throws NoSuchElementException, ConcurrentModificationException{
			if (!this.hasPrevious()){
				throw new NoSuchElementException();
			}
			T data = DoubleLinkedList.this.get(current);
			current--;
			stateTracker++;

			return data;
		}
		
		@Override
		/**
		 * Returns the index value of the previous item in the list that
		 * the iterator is currently pointing at.
		 * 
		 * @return the index of the previous item in the list
		 */
		public int previousIndex() {
			return current-1;
		}

		@Override
		/**
		 * Sets the currently selected index to the user-specified element.
		 * Next/previous must be called at least once initially and also
		 * after each call to remove()/add().
		 * 
		 * @throws IllegalStateException thrown if previous/next have not been called,
		 * 								 or if remove/add have been called without calling
		 * 								 previous/next afterwards.
		 */
		public void set(T element) throws IllegalStateException{
			if(stateTracker <= 0){
				throw new IllegalStateException("neither next nor previous have been called, or remove or add have been called after the last call to next or previous");
			}
			
			DoubleLinkedList.this.set(current, element);
			iteratorModCount++;
		}
	}	
}
