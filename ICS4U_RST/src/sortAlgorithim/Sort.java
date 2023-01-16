package sortAlgorithim;

public class Sort {

	private final static int DESCENDING = 2;
	private final static int ASCENDING = 1;
	private static int loopCounter = 0;
	private static int comparisonCounter = 0;
	private static int shiftCounter = 0;

	/**
	 * Sorts an array of numbers from low to high using the bubble sort algorithm
	 * pre: none post: array has been sorted from low to high
	 */
	public static void bubbleSort(int data[], int order) {
		// We'll use bottom to check a smaller list each time
		int bottom = data.length - 1;

		// When we make it through the list without any swaps we can leave the algorithm
		boolean hadSwap = true;
		while (hadSwap) {
			// if we get through the whole list without a swap, we are done!
			hadSwap = false;

			// Check all elements that might still need to be moved.
			for (int i = 0; i < bottom; i++) {
				loopCounter++;
				comparisonCounter++;
				// check if the two elements are in the correct order
				if ((order == ASCENDING && data[i] > data[i + 1]) || (order == DESCENDING && data[i] < data[i + 1])) {
					hadSwap = true;
					
					// swap values at i and i+1
					int temp = data[i];
					data[i] = data[i + 1];
					data[i + 1] = temp;
					shiftCounter++;
				}
			}
			// Last item is in the correct place - shorten the length of the list
			bottom--;
		}
	}

	/**
	 * Sorts an array of numbers from low to high using the selection sort algorithm
	 * pre: none post: array has been sorted from low to high
	 */
	public static void selectionSort(int[] data, int order) {

		// Variables for array indices
		int index, j, currentMinOrMax;
		// Variable to hold data for the swap
		int temp;

		for (index = 0; index < data.length - 1; index++) {
			loopCounter++;
			// assume the current element is the smallest/largest, until we find another
			// one!
			currentMinOrMax = index;

			// see if there is a smaller or larger number further in the array, depending on
			// order variable
			for (j = index + 1; j < data.length; j++) {
				comparisonCounter++;
				loopCounter++;
				if ((order == ASCENDING && data[j] < data[currentMinOrMax])
						|| (order == DESCENDING && data[j] > data[currentMinOrMax])) {
					// found a new smallest/largest value, keep its location
					currentMinOrMax = j;
				}
			}

			// Don't bother swapping if we actually found the smallest/largest
			comparisonCounter++;
			if (index != currentMinOrMax) {
				// swap values at currentMin and index
				temp = data[currentMinOrMax];
				data[currentMinOrMax] = data[index];
				data[index] = temp;
				shiftCounter++;
			}
		}
	}

	/**
	 * Sorts an array of numbers from low to high using the insertion sort algorithm
	 * pre: none post: array has been sorted from low to high
	 */
	public static void insertionSort(int[] data, int order) {

		// Variables for the array indices
		int index, moveItem;

		// Variable to hold the array element being moved
		int temp;

		for (index = 1; index < data.length; index++) {
			loopCounter++;
			// let's place the next element where it belongs - stored in temp
			temp = data[index];
			// don't want to lose track of index, so copy its value into moveItem
			moveItem = index;

			// Keep going until we get back to the start of the list, or the item is in its
			// correct (relative) place
			while (moveItem > 0 && (order == ASCENDING && temp < data[moveItem - 1])
					|| (order == DESCENDING && temp > data[moveItem - 1])) {
				// moving items over to make room
				loopCounter++;
				comparisonCounter++;
				data[moveItem] = data[moveItem - 1];
				moveItem--;
				shiftCounter++;
			}
			// place the item where it belongs
			data[moveItem] = temp;
			shiftCounter++;
		}
	}

	/**
	 * Sorts an array of numbers from low to high using the merge sort algorithm
	 * pre: low > 0, high > 0 post: array has been sorted from low to high
	 * (increasing)
	 */
	public static void mergeSort(int[] data, int low, int high, int order) {
		// prevent infinite recursion - use an if statement
		loopCounter++;
		if (low < high) {
			int mid = (low + high) / 2;

			// resursive mergeSort
			mergeSort(data, low, mid, order); // sort the left side
			mergeSort(data, mid + 1, high, order); // sort the right side

			// merge both sub-lists TOGETHER
			merge(data, low, mid, high, order);
		}

	}

	/**
	 * Merges two sorted portions of data array private because it should NOT be
	 * called from an outside class! pre: data[start..mid] is sorted.
	 * data[mid+1..end] sorted. start <= mid <= end post: data[start..end] is sorted
	 */
	private static void merge(int[] data, int start, int mid, int end, int order) {

		// Create a temporary array of the same type as data
		int[] temp = new int[data.length];

		// Track position in each sub-list,
		// andhttps://www.youtube.com/watch?v=XaqR3G_NVoo the temp array
		int pos1 = start; // index in left sub-list
		int pos2 = mid + 1; // index in right sub-list
		int index = start;

		// process both lists - getting the smallest from each
		// keep going while both lists still have elements left
		while (pos1 <= mid && pos2 <= end) {
			// element in right list(pos2)is smaller or larger
			comparisonCounter++;
			if ((order == ASCENDING && data[pos2] < data[pos1]) || (order == DESCENDING && data[pos2] > data[pos1])) {
				temp[index] = data[pos2]; // place element from right list in temp array
				shiftCounter++;
				index++;
				pos2++; // move the index to the next element in right sub-list

			} else { // otherwise, other, get element from the left list (pos1)
				shiftCounter++;
				temp[index] = data[pos1];
				index++;
				pos1++; // move the index to the next element in left sub-list
			}
		}

		while (pos1 <= mid) {
			shiftCounter++;
			temp[index] = data[pos1]; // place element from left list in temp array
			index++;
			pos1++; // move the index to the next element in left sub-list
		}

		while (pos2 <= end) {
			shiftCounter++;
			temp[index] = data[pos2]; // place element from right list in temp array
			index++;
			pos2++; // move the index to the next element in right sub-list
		}

		// Copy temp array back into data
		for (int i = start; i <= end; i++) {
			data[i] = temp[i];
		}
	}

	/**
	 * Sorts an array of numbers from low to high using the quick sort algorithm
	 * pre: low > 0, high > 0 post: array has been sorted from low to high
	 * (increasing)
	 */
	public static void quickSort(int[] data, int low, int high, int order) {
		if (low < high) {
			// Create a partition
			int location = partition(data, low, high, order);
			// recursively call quickSort on each half of the list
			quickSort(data, low, location - 1, order);
			quickSort(data, location + 1, high, order);

		}
	}

	/**
	 * Creates a partition within a data array private because it should NOT be
	 * called from an outside class! pre: left < right post: all data from left to j
	 * are less than pivot all data from j to right are greater than pivot
	 */
	private static int partition(int[] data, int left, int right, int order) {

		loopCounter++;
		
		// choose the left-most value as the pivot
		int pivot = data[left];

		// begin j at this position
		int j = left;

		// process every element between left and right
		for (int i = left + 1; i <= right; i++) {
			loopCounter++;
			// check if current element is less than the pivot,
			comparisonCounter++;
			if ((order == ASCENDING && data[i] < pivot) || (order == DESCENDING && data[i] > pivot)) {
				// make room by incrementing j
				j++;
				// swap values at i and j
				int temp = data[i];
				data[i] = data[j];
				data[j] = temp;
				shiftCounter++;

			}
		}
		// swap values at left and j
		int temp = data[left];
		data[left] = data[j];
		data[j] = temp;
		shiftCounter++;

		// return the location that this element ended up at
		return j;

	}

	/**
	 * Sorts an array of numbers from low to high using the bubble sort algorithm
	 * pre: none post: array has been sorted from low to high
	 */
	public static void bubbleSort(String data[]) {
		
		
		// We'll use bottom to check a smaller list each time
		int bottom = data.length - 1;

		// When we make it through the list without any swaps we can leave the algorithm
		boolean hadSwap = true;
		while (hadSwap) {
			// if we get through the whole list without a swap, we are done!
			hadSwap = false;

			// Check all elements that might still need to be moved.
			for (int i = 0; i < bottom; i++) {

				// check if the two elements are in the correct order
				if (data[i].compareTo(data[i + 1]) > 0) {
					hadSwap = true;

					// swap values at i and i+1
					String temp = data[i];
					data[i] = data[i + 1];
					data[i + 1] = temp;
				}
			}
			// Last item is in the correct place - shorten the length of the list
			bottom--;
		}
	}

	/**
	 * Sorts an array of numbers from low to high using the selection sort algorithm
	 * pre: none post: array has been sorted from low to high
	 */
	public static void selectionSort(String[] data) {

		// Variables for array indices
		int index, j, currentMinOrMax;
		// Variable to hold data for the swap
		String temp;

		for (index = 0; index < data.length - 1; index++) {
			// assume the current element is the smallest/largest, until we find another
			// one!
			currentMinOrMax = index;

			// see if there is a smaller or larger number further in the array, depending on
			// order variable
			for (j = index + 1; j < data.length; j++) {
				if (data[j].compareTo(data[currentMinOrMax]) < 0) {
					// found a new smallest/largest value, keep its location
					currentMinOrMax = j;
				}
			}

			// Don't bother swapping if we actually found the smallest/largest
			if (index != currentMinOrMax) {
				// swap values at currentMin and index
				temp = data[currentMinOrMax];
				data[currentMinOrMax] = data[index];
				data[index] = temp;
			}
		}
	}

	/**
	 * Sorts an array of numbers from low to high using the insertion sort algorithm
	 * pre: none post: array has been sorted from low to high
	 */
	public static void insertionSort(String[] data) {

		// Variables for the array indices
		int index, moveItem;

		// Variable to hold the array element being moved
		String temp;

		for (index = 1; index < data.length; index++) {
			// let's place the next element where it belongs - stored in temp
			temp = data[index];
			// don't want to lose track of index, so copy its value into moveItem
			moveItem = index;

			// Keep going until we get back to the start of the list, or the item is in its
			// correct (relative) place
			while (moveItem > 0 && temp.compareTo(data[moveItem - 1]) < 0) {
				// moving items over to make room
				data[moveItem] = data[moveItem - 1];
				moveItem--;
			}
			// place the item where it belongs
			data[moveItem] = temp;
		}
	}

	/**
	 * Sorts an array of numbers from low to high using the merge sort algorithm
	 * pre: low > 0, high > 0 post: array has been sorted from low to high
	 * (increasing)
	 */
	public static void mergeSort(String[] data, int low, int high) {
		// prevent infinite recursion - use an if statement
		if (low < high) {
			int mid = (low + high) / 2;

			// resursive mergeSort
			mergeSort(data, low, mid); // sort the left side
			mergeSort(data, mid + 1, high); // sort the right side

			// merge both sub-lists TOGETHER
			merge(data, low, mid, high);
		}

	}

	/**
	 * Merges two sorted portions of data array private because it should NOT be
	 * called from an outside class! pre: data[start..mid] is sorted.
	 * data[mid+1..end] sorted. start <= mid <= end post: data[start..end] is sorted
	 */
	private static void merge(String[] data, int start, int mid, int end) {

		// Create a temporary array of the same type as data
		String [] temp = new String[data.length];

		// Track position in each sub-list,
		// and https://www.youtube.com/watch?v=XaqR3G_NVoo the temp array
		int pos1 = start; // index in left sub-list
		int pos2 = mid + 1; // index in right sub-list
		int index = start;

		// process both lists - getting the smallest from each
		// keep going while both lists still have elements left
		while (pos1 <= mid && pos2 <= end) {

			// element in right list(pos2)is smaller or larger
			if (data[pos2].compareTo(data[pos1]) < 0) {
				temp[index] = data[pos2]; // place element from right list in temp array
				index++;
				pos2++; // move the index to the next element in right sub-list

			} else { // otherwise, other, get element from the left list (pos1)
				temp[index] = data[pos1];
				index++;
				pos1++; // move the index to the next element in left sub-list
			}
		}

		while (pos1 <= mid) {
			temp[index] = data[pos1]; // place element from left list in temp array
			index++;
			pos1++; // move the index to the next element in left sub-list
		}

		while (pos2 <= end) {
			temp[index] = data[pos2]; // place element from right list in temp array
			index++;
			pos2++; // move the index to the next element in right sub-list
		}

		// Copy temp array back into data
		for (int i = start; i <= end; i++) {
			data[i] = temp[i];
		}
	}

	/**
	 * Sorts an array of numbers from low to high using the quick sort algorithm
	 * pre: low > 0, high > 0 post: array has been sorted from low to high
	 * (increasing)
	 */
	public static void quickSort(String[] data, int low, int high) {
		if (low < high) {
			// Create a partition
			int location = partition(data, low, high);
			// recursively call quickSort on each half of the list
			quickSort(data, low, location - 1);
			quickSort(data, location + 1, high);

		}
	}

	/**
	 * Creates a partition within a data array private because it should NOT be
	 * called from an outside class! pre: left < right post: all data from left to j
	 * are less than pivot all data from j to right are greater than pivot
	 */
	private static int partition(String[] data, int left, int right) {

		// choose the left-most value as the pivot
		String pivot = data[left];

		// begin j at this position
		int j = left;

		// process every element between left and right
		for (int i = left + 1; i <= right; i++) {
			// check if current element is less than the pivot,
			if (data[i].compareTo(pivot) < 0) {
				// make room by incrementing j
				j++;
				// swap values at i and j
				String temp = data[i];
				data[i] = data[j];
				data[j] = temp;

			}
		}
		// swap values at left and j
		String temp = data[left];
		data[left] = data[j];
		data[j] = temp;

		// return the location that this element ended up at
		return j;

	}
	
	static int getLoopCounter() {
		return loopCounter;
	}
	
	static int getComparisonCounter() {
		return comparisonCounter;
	}
	
	static int getShiftCounter() {
		return shiftCounter;
	}
	
	static void resetLoopCounter() {
		loopCounter = 0;
	}
	
	static void resetComparisonCounter() {
		comparisonCounter = 0;
	}
	
	static void resetShiftCounter() {
		shiftCounter = 0;
	}
	
	
}
