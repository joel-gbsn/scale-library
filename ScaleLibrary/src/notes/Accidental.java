package notes;

import java.util.ArrayList;

/**
 * Represents an accidental to apply to a music note.
 * @author Joel Gibson
 */
public class Accidental {
	
	/**
	 * The symbol representing the accidental (e.g. b, #).
	 */
	private String symbol;
	
	/**
	 * The semitone change applied by the accidental.
	 */
	private int semitones;
	
	/**
	 * The list of all accidentals.
	 */
	private static ArrayList<Accidental> accidentals = new ArrayList<Accidental>();
	
	/**
	 * Creates an accidental from the given symbol and semitone change.
	 * @param symbol the symbol representing the accidental
	 * @param semitones the semitone change applied by the accidental
	 */
	public Accidental(String symbol, int semitones) {
		this.symbol = symbol;
		this.semitones = semitones;
		
		accidentals.add(this);
	}
	
	/**
	 * Creates a list of all possible accidentals between 3 flats and 3 sharps.
	 */
	public static void createAllAccidentals() {
		// the possible accidental symbols and corresponding semitone changes (these should be adequate
		// for all commonly encountered intervals)
		String[] symbols = {"bbb", "bb", "b", "", "#", "x", "#x"};
		int[] semitones = {-3, -2, -1, 0, 1, 2, 3};
		
		// create and store each accidental
		for (int i = 0; i < 7; i++) {
			new Accidental(symbols[i], semitones[i]);
		}
	}

	/**
	 * Gets the accidental symbol.
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Gets the semitones change applied by the accidental.
	 * @return the number of semitones
	 */
	public int getSemitones() {
		return semitones;
	}
	
	/**
	 * Gets the list of all accidentals.
	 * @return the accidental list
	 */
	public static ArrayList<Accidental> getAccidentals() {
		return accidentals;
	}
	
	/**
	 * Returns the accidental associated with the given semitone change.
	 * @param semitones the number of semitones to convert
	 * @return the accidental, or null if no accidental matches the requested semitone change
	 */
	public static Accidental convertSemitones(int semitones) {
		// check the semitone change of each accidental
		for (Accidental accidental : getAccidentals()) {
			if (accidental.getSemitones() == semitones) {
				return accidental;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a string representation of the accidental for printing.
	 */
	@Override
	public String toString() {
		return getSymbol();
	}
	
	/**
	 * Compares the accidentals for equality using their symbols.
	 */
	@Override
	public boolean equals(Object accidental) {
		Accidental otherAccidental = (Accidental) accidental;
		
		return this.getSymbol().equals(otherAccidental.getSymbol());
	}
}
