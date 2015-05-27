import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
	
/** Represents a set of Boggle dice. When more dice are needed
 * than are present in the standard 16-dice set, BoggleDice
 * uses multiple sets to maintain letter frequencies. 
 * 
 * @author mvail
 */
public class BoggleDice {
	private Random rand;
	private ArrayList<BoggleDie> baseSet;
	private char[] dice;
	private int numDice;
	
	/** Generate a set of numDice Boggle dice */
	public BoggleDice(int numDice) {
		rand = new Random(); //must come before creation of dice
		//NOTE: an ArrayList is used, rather than an array, in order to
		// take advantage of the Collections.shuffle() method, which is
		// not available for basic arrays
		baseSet = new ArrayList<BoggleDie>();
		baseSet.add(new BoggleDie("RYTTEL"));
		baseSet.add(new BoggleDie("VTHRWE"));
		baseSet.add(new BoggleDie("EGHWNE"));
		baseSet.add(new BoggleDie("SEOTIS"));
		baseSet.add(new BoggleDie("ANAEEG"));
		baseSet.add(new BoggleDie("IDSYTT"));
		baseSet.add(new BoggleDie("OATTOW"));
		baseSet.add(new BoggleDie("MTOICU"));
		baseSet.add(new BoggleDie("AFPKFS"));
		baseSet.add(new BoggleDie("XLDERI"));
		baseSet.add(new BoggleDie("HCPOAS"));
		baseSet.add(new BoggleDie("ENSIEU"));
		baseSet.add(new BoggleDie("YLDEVR"));
		baseSet.add(new BoggleDie("ZNRNHL"));
		baseSet.add(new BoggleDie("NMIQHU"));
		baseSet.add(new BoggleDie("OBBAOJ"));
		
		this.numDice = numDice;
		dice = new char[numDice];
		shuffleDice();
	}
	
	/** Generates a new set of letters from standard sets of
	 * Boggle dice
	 */
	public void shuffleDice() {
		for (int count = 0; count < numDice; count++) {
			if (count % baseSet.size() == 0) {
				shuffleBaseSet();
			}
			dice[count] = baseSet.get(count % baseSet.size()).getLetter();
		}
	}
	
	/** Get the letter at index
	 * @param index location of letter to return
	 * @return letter from die at index
	 */
	public char getLetter(int index) {
		return dice[index];
	}
	
	/** Shuffle the base set of dice */
	private void shuffleBaseSet() {
		Collections.shuffle(baseSet); //reorder dice
		for (BoggleDie die : baseSet) {
			die.roll(); //select a random letter for each die
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer str = new StringBuffer();
		for (char c : dice) {
			str.append(c + " ");
		}
		return str.toString();
	}
	
	////////////////////////////////////////////////////////
	// INNER CLASS: BOGGLEDICE.BOGGLEDIE
	////////////////////////////////////////////////////////
	
	/** Represents one Boggle die that may be rolled to get one of its letters. */
	private class BoggleDie {
		private String letters;
		private char curLetter;
		
		/** @param dieLetters the set of letters that this BoggleDie can return */
		public BoggleDie(String dieLetters) {
			this.letters = dieLetters;
			roll();
		}
		
		/** @return random letter from this BoggleDie */
		public char roll() {
			curLetter = letters.charAt(rand.nextInt(letters.length()));
			return curLetter;
		}
		
		/** @return the current letter */
		public char getLetter() {
			return curLetter;
		}
	} //BoggleDice.BoggleDie inner class
} //BoggleDice class
