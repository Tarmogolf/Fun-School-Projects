import java.util.ListIterator;

/**
 * Combines UnorderedListADT and IndexedListADT interfaces and adds requirement that
 * the list supports a java.util.ListIterator.
 * 
 * @author Java Foundations
 * @version 4.0
 * @author mvail, Stan Bessey
 */
public interface DoubleLinkedListADT<T> extends UnorderedListADT<T>, IndexedListADT<T> {
	/**
	 * Returns a list iterator over the elements in this list (in proper sequence).
	 * 
	 * @return a list iterator over the elements in this list (in proper sequence)
	 */
	public ListIterator<T> listIterator();
	
	/**
	 * Returns a list iterator over the elements in this list (in proper sequence), starting at the specified position in the list. The specified index indicates the first element that would be returned by an initial call to next. An initial call to previous would return the element with the specified index minus one.
	 * 
	 * @param startingIndex index of the first element to be returned from the list iterator (by a call to next)
	 * 
	 * @return a list iterator over the elements in this list (in proper sequence), starting at the specified position in the list
	 * 
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
	 */
	public ListIterator<T> listIterator(int startingIndex);
	
    /**
     * Returns a portion of the current DoubleLinkedList as a new,
     * independent DoubleLinkedList.
     * 
     * @param beginning the index at which to begin the new sublist
     * @param end the index at which to end the new sublist - can go one element "off" of the list, since end is exclusive
     * @return A partial list of the original list from idnex beginning to index end, exclusive.
     */
    public DoubleLinkedListADT<T> subList(int beginning, int end);
	
}
