import java.util.Comparator;

/**
 * Helper class used to sort lists and find the smallest/largest values in
 * lists that are created from classes that implement the DoubleLinkedListADT
 * interface. All methods are static, so there is no need to instantiate
 * a MergeSort object.
 * 
 * @author Stan Bessey
 *
 */
public class MergeSort {

	/**
	 * Sorts a user-specified list from a class that implements the DoubleLinkedListADT
	 * using the elements' default compareTo method.
	 * Elements are sorted in ascending order, smallest to largest.
	 * @param list The list to be sorted. Note that the element type T's class
	 * 			   MUST implement the Comparable<T> interface
	 */
	public static <T extends Comparable<T>> void sort(DoubleLinkedListADT<T> list) {

		if(list.size() > 1){//list is recursively split in half until each segment is only a single element

			int halfSize = list.size()/2;

			//split the parent list into two sublists to be sorted
			DoubleLinkedListADT<T> left = list.subList(0, halfSize);
			DoubleLinkedListADT<T> right = list.subList(halfSize, list.size());

			sort(left);
			sort(right);

			merge(list, left, right); //add the two partial lists back into the parent list in a sorted manner			
		}
	}

	/*
	 * uses the "divide and conquer" method to compare the elements in one sublist (left)
	 * to another sublist (right). When one list runs out of elements, the remaining
	 * elements in the other list are appended to the back of the "parent" list.
	 */
	private static <T extends Comparable<T>> void merge(DoubleLinkedListADT<T> list, 
			DoubleLinkedListADT<T> left, DoubleLinkedListADT<T> right){

		int numTotalElem = left.size() + right.size();

		int rightIndex = 0;
		int listIndex = 0;
		int leftIndex = 0;

		while(listIndex < numTotalElem){
			if(leftIndex < left.size() && rightIndex < right.size()){
				//compares the currently "selected" (as determined by the values
				//of rightIndex and leftIndex) and adds the smaller of the two
				//to the "parent" list that is passed in from the sort method.
				if(left.get(leftIndex).compareTo(right.get(rightIndex)) < 0){
					list.set(listIndex, left.get(leftIndex));
					//moves to the next available spot in the list so that we don't erase
					//the change we just made
					listIndex++;
					//move to the next element in the left list to be compared
					//to the currently selected element in the right list
					leftIndex++;
				}else{
					list.set(listIndex, right.get(rightIndex));
					//moves to the next available spot in the list so that we don't erase
					//the change we just made
					listIndex++;
					//move to the next element in the right list to be compared
					//to the currently selected element in the left list
					rightIndex++;
				}
			}
			else{
				//if one of the two lists "runs out" of items to
				//compare, the remaining elements in the non-exhausted list are
				//added to the end of the "parent" list
				if(leftIndex >= left.size()){
					while(rightIndex < right.size()){
						list.set(listIndex, right.get(rightIndex));
						listIndex++;
						rightIndex++;
					}
				}
				if(rightIndex >= right.size()){
					while(leftIndex < left.size()){
						list.set(listIndex, left.get(leftIndex));
						listIndex++;
						leftIndex++;
					}
				}
			}
		}	
	}

	/**
	 * Sorts a user-specified list from a class that implements the DoubleLinkedListADT
	 * using the user defined Comparator c to put the elements in order.
	 * 
	 * Elements are sorted in ascending order, smallest to largest.
	 * 
	 * @param list The list to be sorted. 
	 * @param c used to override the default compareTo method of class T
	 */
	public static <T> void sort(DoubleLinkedListADT<T> list, Comparator<T> c) {
		if(list.size() > 1){ //list is recursively split in half until each segment is only a single element
			int halfSize = list.size()/2;
			DoubleLinkedListADT<T> left = list.subList(0, halfSize);
			DoubleLinkedListADT<T> right = list.subList(halfSize, list.size());

			sort(left,c);
			sort(right,c);

			//add the two partial lists back into the parent list in a sorted manner
			//based on the user defined Comparator c
			merge(list, left,right,c); 		
		}
	}

	
	/*
	 * uses the "divide and conquer" method to compare the elements in one sublist (left)
	 * to another sublist (right). When one list runs out of elements, the remaining
	 * elements in the other list are appended to the back of the "parent" list.
	 */
	private static <T> void merge(DoubleLinkedListADT<T> list, DoubleLinkedListADT<T> left, 
			DoubleLinkedListADT<T> right, Comparator<T> c){

		int numTotalElem = left.size() + right.size();

		int rightIndex = 0;
		int listIndex = 0;
		int leftIndex = 0;

		while(listIndex < numTotalElem){
			//ensures that each sublist has at least one more element to be compared
			if(leftIndex < left.size() && rightIndex < right.size()){
				//compares the currently "selected" (as determined by the values
				//of rightIndex and leftIndex) and adds the smaller of the two
				//to the "parent" list that is passed in from the sort method.
				if(c.compare(left.get(leftIndex), right.get(rightIndex)) < 0){
					list.set(listIndex, left.get(leftIndex));
					//moves to the next available spot in the list so that we don't erase
					//the change we just made
					listIndex++;
					//move to the next element in the left list to be compared
					//to the currently selected element in the right list
					leftIndex++;
				}else{
					list.set(listIndex, right.get(rightIndex));
					//moves to the next available spot in the list so that we don't erase
					//the change we just made
					listIndex++;
					//move to the next element in the right list to be compared
					//to the currently selected element in the left list
					rightIndex++;
				}
			}
			else{
				//if one of the two lists "runs out" of items to
				//compare, the remaining elements in the non-exhausted list are
				//added to the end of the "parent" list
				if(leftIndex >= left.size()){
					while(rightIndex < right.size()){
						list.set(listIndex, right.get(rightIndex));
						listIndex++;
						rightIndex++;
					}
				}
				if(rightIndex >= right.size()){
					while(leftIndex < left.size()){
						list.set(listIndex, left.get(leftIndex));
						listIndex++;
						leftIndex++;
					}
				}
			}
		}	
	}

	/**
	 * Returns the smallest element in a DoubleLinkedList 
	 * without altering the original list.
	 * @param list The data set to be searched
	 * @return the largest element T in the list
	 */
	public static <T extends Comparable<T>> T findSmallest(DoubleLinkedListADT<T> list) {
		DoubleLinkedListADT<T> temp = list.subList(0, list.size());
		sort(temp);
		return temp.first();
	}

	/**
	 * Returns the smallest element in a DoubleLinkedList as defined by the
	 * user specified custom Comparator without altering the original list.
	 * 
	 * @param list The data set to be searched
	 * @param c User-defined custom comparator
	 * @return the largest element T in the list
	 */
	public static <T> T findSmallest(DoubleLinkedListADT<T> list, Comparator<T> c) {
		DoubleLinkedListADT<T> temp = list.subList(0, list.size());
		sort(temp,c);
		return temp.first();
	}

	/**
	 * Returns the largest element in a DoubleLinkedList 
	 * without altering the original list.
	 * @param list The data set to be searched
	 * @return the largest element T in the list
	 */
	public static <T extends Comparable<T>> T findLargest(DoubleLinkedListADT<T> list) {
		DoubleLinkedListADT<T> temp = list.subList(0, list.size());
		sort(temp);
		return temp.last();
	}

	/**
	 * Returns the largest element in a DoubleLinkedList as defined by the
	 * user specified custom Comparator without altering the original list.
	 * 
	 * @param list The data set to be searched
	 * @param c User-defined custom comparator
	 * @return the largest element T in the list
	 */
	public static <T> T findLargest(DoubleLinkedListADT<T> list, Comparator<T> c) {
		DoubleLinkedListADT<T> temp = list.subList(0, list.size());
		sort(temp,c);
		return temp.last();
	}

}
