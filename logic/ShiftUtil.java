package logic;

import java.util.ArrayList;

import api.Shift;

/**
 * Utility class containing the basic logic for performing moves in the Powers
 * game. All methods operate on a one-dimensional array of integers representing
 * the tiles. A cell with zero is referred to as "empty" and a nonzero cell is
 * "nonempty". Tiles are only shifted to the left; that is, tiles that are moved
 * or merged can only move to the left. The Powers class can use these methods
 * to shift a row or column in any direction by copying that row or column,
 * forward or backward, into a temporary one-dimensional array.
 * 
 * @author Kyle Goben
 */
public class ShiftUtil {
	/**
	 * Returns the index of the first nonempty cell that is on or after the given
	 * index <code>start</code>, or -1 if there is none.
	 * 
	 * @param  arr
	 *               given array
	 * @param  start
	 *               index at which to start looking
	 * @return       index of the first nonempty cell, or -1 if none is found
	 */
	public int findNextNonempty(int[] arr, int start) {
		int index = -1;
		for (int i = start; i < arr.length; i++)
			if (arr[i] != 0) {
				index = i; //steps through the array checking to see if the tile if full or empty
				break;
			}
		return index;
	}

	/**
	 * Given an array and a starting index, finds a shift that would merge or move a
	 * tile to that index, if such a shift exists. This method does not modify the
	 * array. If there is no shift to the given index, returns null. This method is
	 * not required to examine cells to the left of <code>index</code>. Note that in
	 * case of a merge, the value of the Shift object is the <em>current</em> value
	 * on the tile or tiles, not the new value that it would have after the merge
	 * takes place.
	 * 
	 * The logic of this method can be described as follows:
	 * 
	 * <pre>
	 *  if cell at index is occupied (nonzero)
	 *      find next occupied cell c to the right of 'index'
	 *      if there is one and it is the same value
	 *            create a shift to merge c with cell at 'index'
	 *  else
	 *      find next occupied cell c to the right of 'index'
	 *      if there is one
	 *           find next occupied cell c2 to the right of c
	 *           if there is one, and if they are the same value
	 *                create a shift to merge c and c2 into cell at index
	 *           else
	 *                create a shift that just moves c to 'index'
	 *  return the shift object
	 * </pre>
	 * 
	 * @param  arr
	 *               array in which to search for possible shift
	 * @param  index
	 *               index for destination of shift
	 * @return       Shift object describing the shift, or null if there is no shift
	 *               possible
	 */
	public Shift findNextPotentialShift(int[] arr, int index) {
		if (arr[index] != 0) { //if starting index is full
			for (int g = index + 1; g < arr.length; g++) {
				if (arr[g] != 0 & arr[g] != arr[index]) //if the next number is not equal to your starting value exit
					break;
				if (arr[index] == arr[g]) {
					Shift shift1 = new Shift(index, g, index, arr[g]); //if the next number is equal to your starting value make a merge
					return shift1;
				}
			}
			return null;
		} else { //if the starting index is empty
			int next = findNextNonempty(arr, index); //find the first number after the starting index
			if (next == -1) //if there is no other numbers after, return null
				return null;
			for (int i = next + 1; i < arr.length; i++) {
				if (arr[i] != 0 & arr[i] != arr[next]) {//if the 2nd value doesnt equal the first one just shift the first on to your index
					Shift shift1 = new Shift(next, index, arr[next]);
					return shift1;
				}
				if (arr[next] == arr[i]) {
					Shift shift1 = new Shift(next, i, index, arr[next]); //if the 2nd value = the first value, merge and shift to your index
					return shift1;
				}
			}
			Shift shift1 = new Shift(next, index, arr[next]);
			return shift1;
		}

	}

	/**
	 * Updates the array according to the given Shift. This method is not required
	 * to check whether the given Shift describes a move or merge that is actually
	 * correct in the given array.
	 * 
	 * @param arr
	 *              given array to be modified
	 * @param shift
	 *              the shift to be applied to the array
	 */
	public void applyOneShift(int[] arr, Shift shift) {

		if (shift.getOldIndex2() == -1) { //if no merge happened

			arr[shift.getOldIndex()] = 0;
			arr[shift.getNewIndex()] = shift.getValue(); //set the old index to 0 and set the new one to the held value
		} else {

			arr[shift.getOldIndex()] = 0;
			arr[shift.getOldIndex2()] = 0;
			arr[shift.getNewIndex()] = shift.getValue() * 2; //set both of the original positions to 0 and set the new one to double the original values.
		}

	}

	/**
	 * Collapses the array to the left by performing a sequence of shifts, and
	 * returns a list of shifts that were performed.
	 * <p>
	 * The idea is to iterate over the array indices from left to right, finding a
	 * shift to that index and (if one exists) applying it to the array. Note that
	 * according to this logic, shifts do not "cascade": once a cell is merged with
	 * another cell, the resulting cell is not merged again during this operation.
	 * For example, when this method is applied to the array [2, 2, 4], the end
	 * result is [4, 4, 0], not [8, 0, 0].
	 * 
	 * @param  arr
	 *             array to be collapsed
	 * @return     list of all shifts performed in the collapse
	 */
	public ArrayList<Shift> shiftAll(int[] arr) {
		ArrayList<Shift> shiftList = new ArrayList<Shift>();
		for (int i = 0; i < arr.length; i++) {
			if (findNextPotentialShift(arr, i) != null) {

				shiftList.add(findNextPotentialShift(arr, i)); //adds every shift possible to a "shift" array list
				applyOneShift(arr, findNextPotentialShift(arr, i)); //executes all of the possible shifts
			}
		}
		return shiftList;
	}

}
