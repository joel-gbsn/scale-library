package util;

import java.util.HashMap;
import java.util.Map;

public class Alteration {
	
	private String symbol;
	private int semitoneChange;
	
	private static Map<String, Alteration> alterations = createAllAlterations();
	
	public Alteration(String symbol, int semitoneChange) {
		this.symbol = symbol;
		this.semitoneChange = semitoneChange;
	}
	
	public static Alteration getAlteration(String symbol) {
		return alterations.get(symbol);
	}
	
	public static Alteration getAlteration(int semitoneChange) {
		for (Map.Entry<String, Alteration> entry : alterations.entrySet()) {
			if (entry.getValue().semitoneChange == semitoneChange) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	private static Map<String, Alteration> createAllAlterations() {
		Map<String, Alteration> alterations = new HashMap<>();
		String[] symbols = {"bb", "b", "", "#", "x"};
		int[] semitones = {-2, -1, 0, 1, 2};
		
		for (int i = 0; i < symbols.length; i++) {
			alterations.put(symbols[i], new Alteration(symbols[i], semitones[i]));
		}
		return alterations;
	}

	/**
	 * @return the semitoneChange
	 */
	public int getSemitoneChange() {
		return semitoneChange;
	}

	@Override
	public String toString() {
		return symbol;
	}
}
