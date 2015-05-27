import java.awt.Dimension;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * BoggleSearch configures a game of Boggle and finds all words from a dictionary
 * found in the board. The program can either run as an interactive GUI or output
 * the found word list to the console. A board can be randomly generated or loaded
 * from an input file.
 * 
 * @author mvail, Stan Bessey
 */
public class BoggleSearch {
	private final int BOARD_DIMENSION; //can be initialized (once) in constructor

	private BoggleBoard board;
	private Storage<BoggleSearchState> stateStore; //storage container for all valid paths, regardless of returned words
	private ArrayList<BoggleSearchState> foundWordPaths; //storage container for valid paths that return valid words
	@SuppressWarnings("unused")
	private long counter = 0; //tracking the number of states found for fun
	private final int MINIMUM_WORD_LENGTH = 3;	

	private BoggleSearch(int boardSize){

			BOARD_DIMENSION = boardSize;
	}

	/**
	 * Configures the program according to command line options and launches
	 * the word search.
	 * 
	 * @param args
	 * 		boardDimension (positive integer required)<br>
	 * 		one of "-s" for stack or "-q" for queue (required)<br>
	 * 		one of "-c" for console output or "-g" for GUI display (required)<br>
	 * 		filename containing a starting Boggle board configuration (required)
	 * @throws Exception, FileNotFoundException, IllegalArgumentException
	 */
	public static void main(String[] args) throws IllegalArgumentException, FileNotFoundException, Exception {

		if(args.length != 4){//args must be 4 values, no more or less
			throw new IllegalArgumentException("args must contain 4 fields: boardsize, stack/queue, console/GUI, and filename of board, in that order.");
		}

		//make sure that args[0] is an int
		try{
			Integer.parseInt(args[0]);
		}catch(NumberFormatException nfe){
			nfe = new NumberFormatException("args[0] must be an integer");
			System.out.println("NumberFormatException: " + nfe.getMessage());
			System.exit(1);
		}
		
		int boardDimension = Integer.parseInt(args[0]); //integer expected
		String storageMethod = args[1]; //-q or -s expected
		String outputMode = args[2]; //-c or -g expected
		String boggleFile = args[3]; //filename of boggleboard expected

		BoggleSearch mySearch = new BoggleSearch(boardDimension); //construct new BoggleSearch

		mySearch.board =  new BoggleBoard(mySearch.BOARD_DIMENSION, boggleFile); //construct new boggleboard

		//check that either a stack or queue is selected
		if(storageMethod.equals("-s")){
			mySearch.stateStore = new Storage<BoggleSearchState>(Storage.DataStructure.stack);
		} else if (storageMethod.equals("-q")){
			mySearch.stateStore = new Storage<BoggleSearchState>(Storage.DataStructure.queue);
		} else{
			throw new IllegalArgumentException("args[1] must be \"-q\" or \"-s\"");
		}

		mySearch.foundWordPaths = new ArrayList<BoggleSearchState>();
		
		//All paths must be evaluated before we can display anything. User may think the computer has frozen,
		//so it makes sense to show a small dialog that things are working as intended.		
		JOptionPane.showMessageDialog(null, "BoggleBoard is compiling. This may take a while depending on board size.");
		
		mySearch.searchPaths(mySearch.stateStore);

		//sorts the output to make it easier to read
		Collections.sort(mySearch.foundWordPaths, new Comparator<BoggleSearchState>(){
			public int compare(BoggleSearchState first, BoggleSearchState second){
				return first.getWord().compareTo(second.getWord());
			}
		});

		//if console mode is chosen, print each word in the list
		if(outputMode.equals("-c")){
			for(BoggleSearchState b : mySearch.foundWordPaths){
				System.out.println(b.getWord());
			}
			System.out.println("Total number of states: " +mySearch.counter);
		} else if(outputMode.equals("-g")){ //if GUI mode is chose, open new BoggleBoardPanel GUI
			JFrame frame = new JFrame("BoggleSearch");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(new BoggleSearchPanel(mySearch.board, mySearch.BOARD_DIMENSION, mySearch.foundWordPaths));
			frame.setPreferredSize(new Dimension(550,400));
			frame.pack();
			frame.setVisible(true);
		}else{
			throw new IllegalArgumentException("args[2] must be \"-c\" or \"-g\"");
		}
		
	}

	/**
	 * Makes the initial call to the recursive getAdjacentStates() method to find the valid paths
	 * in the boggle board that begin with a given Point (i,j) in the boggle board.
	 * 
	 * @param stateStore the storage container (either a stack or queue) used for storing valid paths
	 * @throws Exception potentially thrown by calling BoggleDictionary() constructor 
	 */
	public void searchPaths(Storage<BoggleSearchState> stateStore) throws Exception{
		for (int i = 0; i < BOARD_DIMENSION; i++){
			for(int j = 0; j < BOARD_DIMENSION; j++){
				BoggleSearchState currentState = new BoggleSearchState(i,j);
				currentState.getAdjacentStates(currentState, stateStore); //kickstarts the finding of each path for a given starting point
				findWords(stateStore, new BoggleDictionary(), foundWordPaths); //finds words after completing every path for a given starting point
			}
		}
	}
	
	/**
	 * This method evaluates each state in our storage container to see if a given word is contained
	 * in our dictionary. It also checks for duplicate words.
	 * 
	 * @param stateStore the storage container (stack or queue) used for storing valid paths
	 * @param dictionary dictionary object that contains a list of all potential valid words
	 * @param foundWordPaths list for storing all states that contain a valid word
	 */
	public void findWords(Storage<BoggleSearchState> stateStore, BoggleDictionary dictionary, ArrayList<BoggleSearchState> foundWordPaths){
		while(!stateStore.isEmpty()){
			boolean duplicate = false;
			BoggleSearchState testState = stateStore.retrieve(); //returns the next item on the stack/queue
			for(BoggleSearchState b : foundWordPaths){
				if(testState.getWord().equals(b.getWord())){ //tests to see if a given word is already in the foundWordPaths list
					duplicate = true;
				}
			}
			//add to list if in the dictionary and is 3 or more letters and is not already in the list
			if(dictionary.contains(testState.getWord()) && testState.getWord().length() >= MINIMUM_WORD_LENGTH && !duplicate){
				foundWordPaths.add(testState);
			}
		}
	}


	////////////////////////////////////////////////////////
	// INNER CLASS: BOGGLESEARCHSTATE
	////////////////////////////////////////////////////////

	/** Represents a path through the current Boggle board. It may or may
	 * not represent a valid word or partial word.
	 * @author mvail
	 */
	public class BoggleSearchState {
		/** sentinal value for the start of a path */
		private final Point INITIAL_POINT = new Point(-1, -1);

		/** occupied positions are part of the path while 
		 * the Points in each position indicate that position's
		 * predecessor, for tracing the order of the path */
		private Point[][] searchPath;

		/** the last Point in the path - next states must extend from this Point */
		private Point lastPoint;

		/** the character sequence formed by the path */
		private String word;

		/** Initialize a search path
		 * @param initialRow starting row coordinate for a search path
		 * @param initialCol starting col coordinate for a search path
		 */
		public BoggleSearchState(int initialRow, int initialCol) {
			searchPath = new Point[BOARD_DIMENSION][BOARD_DIMENSION]; //2D array that is the same size as the board
			lastPoint = new Point(initialRow, initialCol); //must precede call to validState()
			if (inBounds()) {
				//these coordinates have no predecessor, so store sentinel Point
				//as predecessor of this Point in the searchPath
				searchPath[initialRow][initialCol] = INITIAL_POINT;
				//start word with the letter at current lastPoint position
				if (board.charAt(initialRow, initialCol) == 'Q') {
					word = "QU";
				} else {
					word = Character.toString(board.charAt(initialRow, initialCol));
				}
			} else {
				throw new IllegalArgumentException();
			}
		}

		/** Extend a search path to a new position from previousState
		 * @param newRow row coordinate of this position
		 * @param newCol col coordinate of this position
		 * @param previousState path prior to this position
		 */
		public BoggleSearchState(int newRow, int newCol, BoggleSearchState previousState) {
			searchPath = new Point[BOARD_DIMENSION][BOARD_DIMENSION];
			//duplicate the previous state's path for independence between states
			for (int row = 0; row < searchPath.length; row++) {
				for (int col = 0; col < searchPath[row].length; col++) {
					searchPath[row][col] = previousState.searchPath[row][col];
				}
			}
			lastPoint = new Point(newRow, newCol);
			if (inBounds() && searchPath[newRow][newCol] == null) {
				//store previous state's lastPoint as the predecessor of this state's lastPoint
				searchPath[newRow][newCol] = previousState.lastPoint; //need a copy?
				//add current lastPoint's corresponding letter to word
				if (board.charAt(newRow, newCol) == 'Q') {
					word = previousState.getWord() + "QU";
				} else {
					word = previousState.getWord() + Character.toString(board.charAt(newRow, newCol));
				}
			} else {
				throw new IllegalArgumentException();
			}
		}

		/**
		 * Recursive method that checks a given state, and all potential surrounding states. Adds
		 * the state to a storage container if the length of the returned word (be it gibberish or an
		 * actual word) is over 3 letters.
		 * @param currentState
		 * @param stateStore the storage container we are storing Boggle states in
		 */
		public void getAdjacentStates(BoggleSearchState currentState, Storage<BoggleSearchState> stateStore){
			stateStore.store(currentState);//puts the current state into the storage container for evaluation later
			counter++; //used for informational purposes only, I was just curious how many states were found depending on gridsize
			System.out.println(currentState.getWord());
			//looping from -1 to 1 for row and col will check all 8 surrounding tiles as valid paths
			for (int rowDelta = -1; rowDelta <= 1; rowDelta++){
				for (int colDelta = -1; colDelta <= 1; colDelta++){
					int potentialRow = currentState.getRow() + rowDelta;//row value for next possible state
					int potentialCol = currentState.getCol() + colDelta;//column value for next possible state
					if(currentState.validNeighbor(potentialRow, potentialCol)){
						BoggleSearchState potentialState = new BoggleSearchState(potentialRow, potentialCol, currentState);
						//make recursive call if the potentialState is inbounds
						if (potentialState.inBounds()){
							this.getAdjacentStates(potentialState, stateStore);
						}
					}	
				}
			}
		}

		/** @return true if coordinates are in-bounds */
		private boolean inBounds() {
			boolean valid = true;
			if (lastPoint.x < 0 || lastPoint.x >= searchPath.length
					|| lastPoint.y < 0 || lastPoint.y >= searchPath[lastPoint.x].length) {
				valid = false;
			}
			return valid;
		}

		/** Check if a potential next path step is valid
		 * @param row row of potential next step
		 * @param col col of potential next step
		 * @return true if next step is adjacent and available
		 */
		public boolean validNeighbor(int row, int col) {
			boolean isValid = true;
			if (Math.abs(row - lastPoint.x) > 1 || Math.abs(col - lastPoint.y) > 1) {
				isValid = false;
			}
			if (row < 0 || row >= searchPath.length || col < 0 || col >= searchPath[row].length
					|| searchPath[row][col] != null) { //depending on short-circuiting
				isValid = false;
			}
			return isValid;
		}


		/** @return word (or gibberish) formed by this search path */
		public String getWord() {
			return word;
		}

		/** @return copy of the search path */
		public Point[][] getPath() {
			Point[][] copy = new Point[searchPath.length][searchPath.length];
			for (int row = 0; row < searchPath.length; row++) {
				for (int col = 0; col < searchPath[row].length; col++) {
					copy[row][col] = searchPath[row][col];
				}
			}
			return copy;
		}

		/** @return row of last position */
		public int getRow() {
			return lastPoint.x;
		}

		/** @return col of last position */
		public int getCol() {
			return lastPoint.y;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return word;
		}
	} //BoggleSearchState class

}//BoggleSearch class
