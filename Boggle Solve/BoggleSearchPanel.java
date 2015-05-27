import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Constructs the GUI elements of the BoggleSearch program when GUI mode
 * is selected. One half represents the actual Boggle Board, and the 
 * other half represents the list of all valid words found within the
 * given BoggleBoard. The user can click on any given word in the list to highlight
 * the corresponding path on the BoggleBoard game board.
 * 
 * @author Stan Bessey
 *
 */
@SuppressWarnings("serial")
public class BoggleSearchPanel extends JPanel implements ListSelectionListener {

	private JPanel gameBoardPanel; //panel for showing the individual boggle tiles
	private JList<String> wordListPanel; //list of valid English words in the gameboard
	private JScrollPane scrollPane;
	private JButton[][] boggleButtons;
	private ArrayList<BoggleSearch.BoggleSearchState> list; //list of all valid states w/ valid words
	private String[] words; //storage container for just the valid words in list
	
	/**
	 * Constructs the different panels that will be added to the parent panel
	 * in the driver class. Calls a separate method to each half of the panel
	 * for clarity.
	 * 
	 * @param board The board that we will read characters from to build the left half of the GUI
	 * @param boardSize The size of the BoggleBoard.
	 * @param list The list containing valid BoggleSearchStates w/ valid words
	 */
	public BoggleSearchPanel(BoggleBoard board, int boardSize, ArrayList<BoggleSearch.BoggleSearchState> list){

		this.list = list;
		
		this.setLayout(new BorderLayout());
		configureGameBoardPanel(boardSize, board);
		configureWordListPanel(list, words);
	}
	
	/**
	 * Constructs a grid of buttons with labels containing the character
	 * at each corresponding position in the BoggleBoard that is passed in.
	 * "Q" is changed to "Qu", as in a normal game of Boggle.
	 * 
	 * @param boardSize The size of the BoggleBoard.
	 * @param board The board that we will read characters from to build the left half of the GUI
	 */
	public void configureGameBoardPanel(int boardSize, BoggleBoard board){
		
		gameBoardPanel = new JPanel();
		gameBoardPanel.setLayout(new GridLayout(boardSize, boardSize));
		gameBoardPanel.setPreferredSize(new Dimension(400, 400));
		boggleButtons = new JButton[boardSize][boardSize];
		
		//iterate through each position in the board and retrieve the letter
		//create a new JButton at the same "coordinates" and set its label
		//to the given character.
		for (int i = 0; i < boardSize; i++){
			for (int j = 0; j < boardSize; j++){
				String label = String.valueOf(board.charAt(i, j));
				if(label.equals("Q")){
					label = "Qu";//to match up to a real game of Boggle
				}
				boggleButtons[i][j] = new JButton(label);
				boggleButtons[i][j].setBackground(Color.WHITE);
				boggleButtons[i][j].setFont(new Font( "sanserif", Font.BOLD, 42));
				gameBoardPanel.add(boggleButtons[i][j]);
			}
		}
		
		this.add(gameBoardPanel,BorderLayout.WEST);
	}
	
	/**
	 * Creates a new JScrollPane + JList to represent a list of all
	 * @param list The list containing valid BoggleSearchStates w/ valid words
	 * @param words Array containing just the words from each BoggleSearchState.
	 */
	public void configureWordListPanel(ArrayList<BoggleSearch.BoggleSearchState> list, String[] words){
		this.words = words;
		words = new String[list.size()];
		
		for(int i = 0; i < list.size(); i++){
			words[i] = list.get(i).getWord(); //creates an array of just the words contained in the list
		}
		
		wordListPanel = new JList<String>(words);
		wordListPanel.addListSelectionListener(this);
		wordListPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //only needed for very long words
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(wordListPanel);
		
		this.add(scrollPane, BorderLayout.CENTER);
	}


	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		String selectedWord = wordListPanel.getSelectedValue();
		highlightWord(selectedWord, list);
	}
	

	
	/**
	 * Compares a user-selected word against each valid state until it finds the state
	 * that is equal to the user-selected word. Each corresponding letter on the BoggleBoard
	 * is highlighted by calling the getPath() function for the matching state.
	 * 
	 * @param selectedWord the word selected by the user in the wordListPanel
	 * @param list the list of BoggleSearchStates w/ valid words
	 */
	public void highlightWord(String selectedWord, ArrayList<BoggleSearch.BoggleSearchState> list){

		for(BoggleSearch.BoggleSearchState state : list){
			if(selectedWord.equals(state.getWord())){
				for(int i = 0; i< state.getPath().length; i++){
					for(int j = 0; j< state.getPath().length; j++){
						//where points are placed in the state path directly relates to where the letter
						//falls in the BoggleBoard. See README for a more detailed description.
						if(state.getPath()[i][j] != null){
							boggleButtons[i][j].setBackground(Color.GREEN); //highlight found words in green
						}else{
							boggleButtons[i][j].setBackground(Color.WHITE); //sets all points not in the path to white to "reset" the board
						}
					}
				}
			}
		}
	}
}
