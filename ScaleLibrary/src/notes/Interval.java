package notes;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a musical interval.
 * @author Joel Gibson
 */
public class Interval {
	
	/**
	 * The size of the interval.
	 */
	private int number;
	
	/**
	 * The quality of the interval.
	 */
	private Accidental quality;
	
	/**
	 * A mapping of all interval names to their associated Interval objects.
	 */
	private static Map<String, Interval> intervals = new HashMap<String, Interval>();
	
	/**
	 * Creates an interval from the given interval size and quality.
	 * @param number the size of the interval
	 * @param quality the quality of the interval (represented by an accidental)
	 */
	public Interval(int number, Accidental quality) {
		this.number = number;
		this.quality = quality;
		
		// store the interval using its string representation
		intervals.put(quality.toString() + number, this);
	}
	
	/**
	 * Gets the interval size.
	 * @return the interval size
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Gets the interval quality (as an accidental).
	 * @return the interval quality
	 */
	public Accidental getQuality() {
		return quality;
	}
	
	/**
	 * Gets the map containing all intervals.
	 * @return the map of intervals
	 */
	public static Map<String, Interval> getIntervals() {
		return intervals;
	}
	
	/**
	 * Creates all common intervals within 2 octaves.
	 */
	public static void createAllIntervals() {
		// create each interval up to a 15th (2 octaves)
		for (int number = 1; number <= 15; number++) {
			for (Accidental quality : Accidental.getAccidentals()) {
				new Interval(number, quality);
			}
		}
	}
	
	/**
	 * Converts the interval size into a simple interval size (within 1 octave).
	 * @return the simple interval size
	 */
	int simplifyNumber() {
		// subtract octaves (7 notes) until interval is within one octave
		int simpleNumber = getNumber();
		while (simpleNumber > 7) {
			simpleNumber -= 7;
		}
		
		return simpleNumber;
	}
	
	/**
	 * Returns the Interval object associated with the given string representation of the interval.
	 * @param interval the interval as a string (e.g. 2, #4 or b7)
	 * @return the Interval, or null if the interval was not found
	 */
	public static Interval getInterval(String interval) {
		// search the map of all intervals
		return intervals.get(interval);
	}
	
	/**
	 * Counts the number of semitones between the given notes.
	 * @param firstNote the lower note
	 * @param secondNote the upper note
	 * @return the number of semitones
	 */
	public static int countSemitones(Note firstNote, Note secondNote) {
		// the array of all possible chromatic notes, used for counting semitone changes
		String[] chromaticNotes = {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A", "Bb", "B"};

		// find the index of the lower note's letter name
		int i = 0;
		while (!firstNote.getLetterName().equals(chromaticNotes[i])) {
			i++;
		}
		
		// account for the accidental of the lower note
		int start = i + firstNote.getAccidental().getSemitones();
		
		// find the number of positions to the second note's letter name, wrapping around the array if necessary
		while (!secondNote.getLetterName().equals(chromaticNotes[i % 12])) {
			i++;
		}
		
		// account for the accidental of the upper note
		int end = i + secondNote.getAccidental().getSemitones();
		
		// calculate the change in semitones
		return end - start;
	}
	
	/**
	 * Converts the given interval into its equivalent number of semitones as a simple interval.
	 * @param interval the interval to convert
	 * @return the number of semitones
	 */
	public static int intervalToSemitones(Interval interval) {
		// the pattern of tones (T) and semitones (S) in a major scale
		String[] tonePattern = {"T", "T", "S", "T", "T", "T", "S"};
		
		// count the semitones according to the major scale pattern, starting from an interval of a 2nd
		int semitones = 0;
		for (int i = 2; i <= interval.simplifyNumber(); i++) {
			// add 1 for each semitone and 2 for each tone
			if ("S".equals(tonePattern[(i - 2) % 7])) {
				semitones += 1;
			} else {
				semitones += 2;
			}
		}
		
		// account for the quality of the interval
		return semitones + interval.getQuality().getSemitones();
	}
	
	/**
	 * Returns a string representation of the interval for printing.
	 */
	@Override
	public String toString() {
		return getQuality().toString() + getNumber();
	}
}
