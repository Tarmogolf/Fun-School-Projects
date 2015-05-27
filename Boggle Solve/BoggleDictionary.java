//	BoggleDictionary.java

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Iterator;

/** 
	A class that stores a dictionary containing words that can be used in a
	Boggle game.

	@author Teresa Cole
	@version CS221
 */
public class BoggleDictionary
{
	private HashSet<String> dictionary;

	/** Create the BoggleDictionary from the file dictionary.dat
	 */
	@SuppressWarnings("unchecked")
	public BoggleDictionary() throws Exception {
		ObjectInputStream dictFile = new ObjectInputStream(
				new FileInputStream( new File( "dictionary.dat")));
		dictionary = (HashSet<String>)dictFile.readObject();
		dictFile.close();
	}

	/** Check to see if a string is in the dictionary to determine whether it
	 * is a valid word.
	 * @param word the string to check for
	 * @return true if word is in the dictionary, false otherwise.
	 */
	public boolean contains( String word)
	{
		return dictionary.contains( word);
	}

	/** Get an iterator that returns all the words in the dictionary, one at a
	 * time.
	 * @return an iterator that can be used to get all the words in the
	 * dictionary.
	 */
	public Iterator<String> iterator() 
	{
		return dictionary.iterator();
	}

	/** 
	 Main entry point
	 */
	static public void main(String[] args)  
	{
		System.out.println( "BoggleDictionary Program ");

		Scanner kbd = new Scanner( System.in);
		BoggleDictionary theDictionary = null;
		try 
		{
			theDictionary = new BoggleDictionary();
		}
		catch (Exception ioe) 
		{
			System.err.println( "error reading dictionary");
			System.exit(1);
		}
		String word;

		
	while (kbd.hasNext())
	    {
	    word = kbd.next();
	    if (theDictionary.contains( word))
		System.out.println( word + " is in the dictionary");
	    else
		System.out.println( word + " is not in the dictionary");
	    }
		 

		Iterator<String> iter = theDictionary.iterator();
		for (int i=0; i<25; i++)
			System.out.println( iter.next()); 
	}

}
