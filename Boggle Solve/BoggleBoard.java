import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/** 
 * Represents a Boggle game board with letters loaded from a configuration file.
 * 
 * @author mvail
 */
public class BoggleBoard {
	private char[][] board;
	
	/** Construct a BoggleBoard from a given board config file.
	 * May throw a wide assortment of Exceptions if parsing the
	 * file is not a valid config file.
	 * 
	 * @param boardDimension
	 * 		dimension N for the NxN grid
	 * @param filename
	 * 		file containing a grid of letters 
	 * @throws FileNotFoundException
	 */
	public BoggleBoard(int boardDimension, String filename) throws FileNotFoundException {
		board = new char[boardDimension][boardDimension];
		Scanner fileScan = new Scanner(new File(filename));
		String line;
		for (int row = 0; row < boardDimension; row++) {
			line = fileScan.nextLine().toUpperCase();
			int charIdx = 0;
			for (int col = 0; col < boardDimension; col++) {
				while (!Character.isLetter(line.charAt(charIdx))) {
					charIdx++;
				}
				board[row][col] = line.charAt(charIdx);
				charIdx++;
			}
		}
		fileScan.close();
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			throw new IllegalArgumentException();
		}
		return board[row][col];
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
	/** @return copy of board */
	public char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
}//BoggleBoard class
