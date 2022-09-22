package logic;

import static api.Direction.DOWN;
import static api.Direction.LEFT;
import static api.Direction.RIGHT;
import static api.Direction.UP;

import java.util.ArrayList;
import java.util.Random;

import api.Descriptor;

import api.Direction;
import api.MoveResult;
import api.Shift;
import api.TilePosition;

/**
 * The Powers class contains the state and logic for an implementation of a game
 * similar to "2048". The basic underlying state is an n by n grid of tiles,
 * represented by integer values. A zero in a cell is considered to be "empty",
 * and all other cells contain some power of two. The game is played by calling
 * the method <code>shiftGrid()</code>, selecting one of the four directions
 * (LEFT, RIGHT, UP, DOWN). Each row or column is then collapsed according to
 * the algorithm described in the methods of <code>ShiftUtil</code>.
 * <p>
 * Whenever two cells are <em>merged</em>, the score is increased by the
 * combined value of the two cells.
 * 
 * @author Kyle Goben
 */
public class Powers {
	/**
	 * Array of rows and columns of tile values.
	 * 
	 */
	private int[][] numbers;
	/**
	 * Random object for generating random numbers
	 */
	private Random rand;
	/**
	 * Number of rows and columns in the game
	 */
	private int size;
	/**
	 * Object of shift used in
	 */
	@SuppressWarnings("unused")
	private ShiftUtil shift;
	/**
	 * Position of a new random tile
	 */
	private TilePosition tile;
	/**
	 * Current score of the game
	 */
	private int score;

	/**
	 * Constructs a game with a grid of the given size, using a default random
	 * number generator. Initially there should be two nonempty cells in the grid
	 * selected by the method <code>generateTile()</code>.
	 * 
	 * @param givenSize
	 *                  size of the grid for this game
	 * @param givenUtil
	 *                  instance of ShiftUtil to be used in this game
	 */
	public Powers(int givenSize, ShiftUtil givenUtil) {
		size = givenSize;
		score = 0;
		numbers = new int[size][size];
		for (int i = 0; i < numbers.length; i++)
			for (int g = 0; g < numbers.length; g++) // sets all tiles equal to 0
				numbers[i][g] = 0;
		rand = new Random();
		shift = givenUtil;
		tile = generateTile();
		numbers[tile.getRow()][tile.getCol()] = tile.getValue(); // adding the newly generated tiles
		tile = generateTile();
		numbers[tile.getRow()][tile.getCol()] = tile.getValue();

	}

	/**
	 * Constructs a game with a grid of the given size, using the given instance of
	 * <code>Random</code> for its random number generator. Initially there should
	 * be two nonempty cells in the grid selected by the method
	 * <code>generateTile()</code>.
	 * 
	 * @param givenSize
	 *                    size of the grid for this game
	 * @param givenUtil
	 *                    instance of ShiftUtil to be used in this game
	 * @param givenRandom
	 *                    given instance of Random
	 */
	public Powers(int givenSize, ShiftUtil givenUtil, Random givenRandom) {
		size = givenSize;
		score = 0;
		numbers = new int[size][size];
		for (int i = 0; i < numbers.length; i++)
			for (int g = 0; g < numbers.length; g++) // Sets all tiles to 0
				numbers[i][g] = 0;
		rand = givenRandom; //sets random to given instance
		shift = givenUtil;
		tile = generateTile();
		numbers[tile.getRow()][tile.getCol()] = tile.getValue(); // adding the newly generated tiles
		tile = generateTile();
		numbers[tile.getRow()][tile.getCol()] = tile.getValue();
	}

	/**
	 * Returns the value in the cell at the given row and column.
	 * 
	 * @param  row
	 *             given row
	 * @param  col
	 *             given column
	 * @return     value in the cell at the given row and column
	 */
	public int getTileValue(int row, int col) {

		return numbers[row][col];
	}

	/**
	 * Sets the value of the cell at the given row and column. <em>NOTE: This method
	 * should not normally be used by clients outside of a testing environment.</em>
	 * 
	 * @param row
	 *              given row
	 * @param col
	 *              given col
	 * @param value
	 *              value to be set
	 */
	public void setValue(int row, int col, int value) {
		numbers[row][col] = value;
	}

	/**
	 * Returns the size of this game's grid.
	 * 
	 * @return size of the grid
	 */
	public int getSize() {

		return size;
	}

	/**
	 * Returns the current score.
	 * 
	 * @return score for this game
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Copy a row or column from the grid into a new one-dimensional array. There
	 * are four possible actions depending on the given direction:
	 * <ul>
	 * <li>LEFT - the row indicated by the index <code>rowOrColumn</code> is copied
	 * into the new array from left to right
	 * <li>RIGHT - the row indicated by the index <code>rowOrColumn</code> is copied
	 * into the new array in reverse (from right to left)
	 * <li>UP - the column indicated by the index <code>rowOrColumn</code> is copied
	 * into the new array from top to bottom
	 * <li>DOWN - the row indicated by the index <code>rowOrColumn</code> is copied
	 * into the new array in reverse (from bottom to top)
	 * </ul>
	 * 
	 * @param  rowOrColumn
	 *                     index of the row or column
	 * @param  dir
	 *                     direction from which to begin copying
	 * @return             array containing the row or column
	 */
	public int[] getRowOrColumn(int rowOrColumn, Direction dir) {
		int[] roc = new int[size];
		int x = 0;
		if (dir.equals(UP))
			for (int i = size - 1; i > -1; i--)
				roc[i] = numbers[i][rowOrColumn]; // reads from the top down from the given column
		if (dir.equals(DOWN))
			for (int i = size - 1; i > -1; i--)
				roc[x++] = numbers[i][rowOrColumn]; // reads from the bottom up from the given column
		if (dir.equals(LEFT))
			for (int i = 0; i < size; i++)
				roc[i] = numbers[rowOrColumn][i]; // reads from left to right from the given row
		if (dir.equals(RIGHT))
			for (int i = size - 1; i > -1; i--)
				roc[x++] = numbers[rowOrColumn][i]; // reads from the right to left from the given row

		return roc;
	}

	/**
	 * Updates the grid by copying the given one-dimensional array into a row or
	 * column of the grid. There are four possible actions depending on the given
	 * direction:
	 * <ul>
	 * <li>LEFT - the given array is copied into the the row indicated by the index
	 * <code>rowOrColumn</code> from left to right
	 * <li>RIGHT - the given array is copied into the the row indicated by the index
	 * <code>rowOrColumn</code> in reverse (from right to left)
	 * <li>UP - the given array is copied into the column indicated by the index
	 * <code>rowOrColumn</code> from top to bottom
	 * <li>DOWN - the given array is copied into the column indicated by the index
	 * <code>rowOrColumn</code> in reverse (from bottom to top)
	 * </ul>
	 * 
	 * @param arr
	 *                    the array from which to copy
	 * @param rowOrColumn
	 *                    index of the row or column
	 * @param dir
	 *                    direction from which to begin copying
	 */
	public void setRowOrColumn(int[] arr, int rowOrColumn, Direction dir) {
		int x = 0;
		if (dir.equals(UP))
			for (int i = 0; i < arr.length; i++)
				numbers[i][rowOrColumn] = arr[i]; // sets the given column starting from the top down
		if (dir.equals(DOWN))
			for (int i = arr.length - 1; i > -1; i--)
				numbers[x++][rowOrColumn] = arr[i]; // sets the given column starting from the bottom up
		if (dir.equals(LEFT))
			for (int i = 0; i < arr.length; i++)
				numbers[rowOrColumn][i] = arr[i]; // sets the given row from left to right
		if (dir.equals(RIGHT))
			for (int i = arr.length - 1; i > -1; i--)
				numbers[rowOrColumn][x++] = arr[i]; // sets the given row from right to left
	}

	/**
	 * Plays one step of the game by shifting the grid in the given direction.
	 * Returns a <code>MoveResult</code> object containing a list of Descriptor
	 * objects describing all moves performed, and a <code>TilePosition</code>
	 * object describing the position and value of a newly added tile, if any. If no
	 * tiles are actually moved, the returned <code>MoveResult</code> object
	 * contains an empty list and has a null value for the new
	 * <code>TilePosition</code>.
	 * <p>
	 * If any tiles are moved or merged, a new tile is selected according to the
	 * <code>generate()</code> method and is added to the grid.
	 * <p>
	 * The shifting of each individual row or column must be performed by the method
	 * <code>shiftAll</code> of <code>ShiftUtil</code>.
	 * 
	 * @param  dir
	 *             direction in which to shift the grid
	 * @return     MoveResult object containing move descriptors and new tile
	 *             position
	 */
	public MoveResult doMove(Direction dir) {
		MoveResult move = new MoveResult();
		ShiftUtil shift = new ShiftUtil();
		ArrayList<Shift> shiftList;
		Descriptor des;
		int[] hold = new int[size]; // temporary holder for a row or column
		for (int i = 0; i < size; i++) {
			hold = getRowOrColumn(i, dir);
			shiftList = shift.shiftAll(hold); // gets list of shifts from given row or column

			setRowOrColumn(hold, i, dir); // uses the temporary array to rewrite the shifter row or column back into the
											// game
			for (int g = 0; g < shiftList.size(); g++) {
				des = new Descriptor(shiftList.get(g), i, dir); // states what just happened
				if (des.isMerge())
					score += des.getValue() * 2; // adds up the score if a merge occured
				move.addMove(des);

			}
		}

		TilePosition tile = generateTile();
		move.setNewTile(tile);
		numbers[tile.getRow()][tile.getCol()] = tile.getValue(); // generates a new tile to add to the game after
																	// each move

		return move;
	}

	/**
	 * Use this game's instance of <code>Random</code> to generate a new tile. The
	 * tile's row and column must be an empty cell of the grid, and the tile's value
	 * is either 2 or 4. The tile is selected in such a way that
	 * <ul>
	 * <li>All empty cells of the grid are equally likely
	 * <li>The tile's value is a 2 with 90% probability and a 4 with 10% probability
	 * </ul>
	 * This method does not modify the grid. If the grid has no empty cells, returns
	 * null.
	 * 
	 * @return a new TilePosition containing the row, column, and value of the
	 *         selected new tile, or null if the grid has no empty cells
	 */
	public TilePosition generateTile() {
		boolean t = true;
		for (int i = 0; i < size; i++)
			for (int g = 0; g < size; g++)
				if (numbers[i][g] == 0) {
					t = false; // finds if an empty space in the game exists
					break;
				}
		if (t)
			return null; // stop and return null if all spaces are full
		int row = rand.nextInt(size);
		int col = rand.nextInt(size);
		while (numbers[row][col] != 0) { // generates a random location until that location is empty
			row = rand.nextInt(size);
			col = rand.nextInt(size);
		}
		int x = rand.nextInt(10);
		int p = 4;
		if (x < 9) // gives a 90% chance for the tile value generated to be 2 and 10% to be 4
			p = 2;

		TilePosition pos = new TilePosition(row, col, p); // makes a new tile position and tile value
		return pos;
	}

}
