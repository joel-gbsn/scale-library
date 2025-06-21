package util;

import java.util.regex.Pattern;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class Interval {
	public String symbol;
	private int size;
	private int semitoneChange;
	
	private static Pattern pattern;
	
	private static Map<String, Interval> intervals = new HashMap<>();
	
	protected Interval(String symbol, int size, int semitoneChange) {
		this.symbol = symbol;
		this.size = size;
		this.semitoneChange = semitoneChange;
	}
	
	public static Interval getInterval(String interval) {
		if (intervals.containsKey(interval)) {
			return intervals.get(interval);
		}
		
		Interval newInterval = Interval.parseInterval(interval);
		if (newInterval != null) {
			intervals.put(interval, newInterval);
		}
		return newInterval;
	}
	
	protected static Interval parseInterval(String interval) {
		if (pattern == null) {
			String regex = "^(?<quality>[#bx]?|bb)(?<size>[0-9]{1,2})$";
			pattern = Pattern.compile(regex);
		}
		
		Matcher matcher = pattern.matcher(interval);
		if (!matcher.matches()) {
			return null;
		}
		
		int size = Integer.parseInt(matcher.group("size"));
		if (size == 0 || size > 15) {
			return null;
		}
		
		return new Interval(interval, size, calculateSemitones(matcher.group("quality")));
	}
	
	protected static int calculateSemitones(String quality) {
		switch(quality) {
		case "#":
			return 1;
		case "x":
			return 2;
		case "b":
			return -1;
		case "bb":
			return -2;
		default:
			return 0;
		}
	}
	
	/**
	 * Converts the interval size into a simple interval size (within 1 octave).
	 * @return the simple interval size
	 */
	int simplifyNumber() {
		return (size - 1) % 7 + 1;
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
		while (!firstNote.letter.equals(chromaticNotes[i])) {
			i++;
		}
		
		// account for the accidental of the lower note
		int start = i + firstNote.semitoneChange;
		
		// find the number of positions to the second note's letter name, wrapping around the array if necessary
		while (!secondNote.letter.equals(chromaticNotes[i % 12])) {
			i++;
		}
		
		// account for the accidental of the upper note
		int end = i + secondNote.semitoneChange;
		
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
		return semitones + interval.semitoneChange;
	}
	
	public String toString() {
		return symbol;
	}
}
