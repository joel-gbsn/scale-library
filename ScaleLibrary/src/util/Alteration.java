package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an semitone alteration to a note or interval.
 * @author Joel Gibson
 */
public class Alteration {
	/**
	 * The symbol representing the alteration (e.g. 'b' or '#')
	 */
	private String symbol;
	
	/**
	 * The number of semitones changed by the alteration.
	 */
	private int semitoneChange;
	
	/**
	 * Maps alteration symbols to the associated Alteration object.
	 */
	protected static Map<String, Alteration> alterations = createAllAlterations();
	
	/**
	 * Creates a new alteration with the given symbol and semitone change.
	 * @param symbol the alteration symbol
	 * @param semitoneChange the number of semitones changed by the alteration
	 */
	protected Alteration(String symbol, int semitoneChange) {
		this.symbol = symbol;
		this.semitoneChange = semitoneChange;
	}
	
	/**
	 * @return the semitoneChange
	 */
	public int getSemitoneChange() {
		return semitoneChange;
	}
	
	/**
	 * Gets the Alteration object associated with the given symbol.
	 * @param symbol the symbol representing the alteration (e.g. 'b' or '#')
	 * @return the Alteration object, or null if the symbol was not valid
	 */
	public static Alteration getAlteration(String symbol) {
		return alterations.get(symbol);
	}
	
	/**
	 * Gets the Alteration object associated with the given semitone change.
	 * @param semitoneChange the number of semitones
	 * @return the Alteration object, or null if the semitone change was not valid
	 */
	public static Alteration getAlteration(int semitoneChange) {
		for (Map.Entry<String, Alteration> entry : alterations.entrySet()) {
			if (entry.getValue().semitoneChange == semitoneChange) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Creates all possible alterations between -2 and +2 semitones.
	 * @return the list of alterations
	 */
	protected static Map<String, Alteration> createAllAlterations() {
		// symbols and their associated semitone changes
		String[] symbols = {"bb", "b", "", "#", "x"};
		int[] semitones = {-2, -1, 0, 1, 2};
		
		// create each alteration
		Map<String, Alteration> alterations = new HashMap<>();
		for (int i = 0; i < symbols.length; i++) {
			alterations.put(symbols[i], new Alteration(symbols[i], semitones[i]));
		}
		return alterations;
	}

	@Override
	public String toString() {
		return symbol;
	}
}
