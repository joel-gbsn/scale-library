package util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Represents a musical interval.
 * @author Joel Gibson
 */
public class Interval {
	/**
	 * The interval number.
	 */
	private int size;
	
	/**
	 * The interval quality, represented as the deviation from a major/perfect interval.
	 */
	private Alteration quality;
	
	/**
	 * The regex pattern for parsing interval names.
	 */
	private static Pattern pattern;
	
	/**
	 * Maps intervals names to the associated Interval objects.
	 */
	private static Map<String, Interval> intervals = new HashMap<>();
	
	/**
	 * The pattern of tones (T) and semitones (S) in a major scale.
	 */
	private static String[] tonePattern = {"T", "T", "S", "T", "T", "T", "S"};
	
	/**
	 * The sequence of all chromatic note names.
	 */
	private static String[] chromaticNotes = {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A", "Bb", "B"};
	
	/**
	 * Creates a new Interval with the given size and quality.
	 * @param size the interval number
	 * @param quality the interval quality (e.g. '#' for augmented)
	 */
	protected Interval(int size, String quality) {
		this.size = size;
		this.quality = Alteration.getAlteration(quality);
	}
	
	/**
	 * Static factory method for creating/retrieving the Interval object associated with the given name.
	 * @param intervalName the name of the interval
	 * @return the Interval object, or null if interval name was invalid
	 */
	public static Interval getInterval(String intervalName) {
		// get interval if it has already been created
		if (intervals.containsKey(intervalName)) {
			return intervals.get(intervalName);
		}
		
		// create new interval
		Interval interval = parseInterval(intervalName);
		if (interval != null) {
			intervals.put(intervalName, interval);
		}
		return interval;
	}
	
	/**
	 * Creates an Interval object using the given interval name.
	 * @param intervalName the interval name
	 * @return the Interval object, or null if interval name was not valid
	 */
	protected static Interval parseInterval(String intervalName) {
		// create regex pattern for extracting the interval components
		if (pattern == null) {
			String regex = "^(?<quality>[#bx]?|bb)(?<size>[0-9]{1,2})$";
			pattern = Pattern.compile(regex);
		}
		
		Matcher matcher = pattern.matcher(intervalName);
		if (!matcher.matches()) {
			return null;
		}
		
		// check if size is valid (maximum 2 octaves = 15 notes)
		int size = Integer.parseInt(matcher.group("size"));
		if (size == 0 || size > 15) {
			return null;
		}
		
		return new Interval(size, matcher.group("quality"));
	}
	
	/**
	 * @return the number of semitones the quality changes the interval by.
	 */
	public int getSemitoneChange() {
		return quality.getSemitoneChange();
	}
	
	/**
	 * Converts the interval size into a simple interval size (within 1 octave).
	 * @return the simple interval size
	 */
	int simplifySize() {
		return (size - 1) % 7 + 1;
	}
	
	/**
	 * Counts the number of semitones between the given notes (within 1 octave).
	 * @param firstNote the lower note
	 * @param secondNote the upper note
	 * @return the number of semitones
	 */
	public static int countSemitones(Note firstNote, Note secondNote) {
		// find the index of the lower note's letter name in the chromatic notes array
		int i = 0;
		while (!firstNote.getLetter().equals(chromaticNotes[i])) {
			i++;
		}
		
		// account for the accidental of the lower note
		int start = i + firstNote.getSemitoneChange();
		
		// find the number of positions to the second note's letter name in the chromatic notes array,
		// wrapping around the array if necessary
		while (!secondNote.getLetter().equals(chromaticNotes[i % 12])) {
			i++;
		}
		
		// account for the accidental of the upper note
		int end = i + secondNote.getSemitoneChange();
		
		// calculate the change in semitones
		return end - start;
	}
	
	/**
	 * Converts the given interval into its equivalent number of semitones as a simple interval.
	 * @param interval the interval to convert
	 * @return the number of semitones
	 */
	public static int intervalToSemitones(Interval interval) {
		// count the semitones according to the major scale pattern, starting from an interval of a major 2nd
		int semitones = 0;
		for (int i = 2; i <= interval.simplifySize(); i++) {
			// add 1 for each semitone and 2 for each tone
			if ("S".equals(tonePattern[(i - 2) % 7])) {
				semitones += 1;
			} else {
				semitones += 2;
			}
		}
		
		// account for the quality of the interval
		return semitones + interval.getSemitoneChange();
	}
	
	@Override
	public String toString() {
		return quality + Integer.toString(size);
	}
}
