package util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Note {
	
	String letter;
	String accidental;
	int semitoneChange;
	
	public static final String[] LETTER_NAMES = {"A", "B", "C", "D", "E", "F", "G"};
	
	private static Pattern pattern;
	
	private static Map<String, Note> notes = new HashMap<>();
	
	protected Note(String letter, String accidental) {
		this.letter = letter;
		this.accidental = accidental;
		this.semitoneChange = calculateSemitones(accidental);
	}
	
	public static Note getNote(String note) {
		if (notes.containsKey(note)) {
			return notes.get(note);
		}
		
		Note newNote = Note.parseNote(note);
		if (newNote != null) {
			notes.put(note, newNote);
		}
		return newNote;
	}
	
	protected static Note parseNote(String note) {
		if (pattern == null) {
			String regex = "^(?<letter>[a-gA-G])(?<accidental>[#bx]?|bb)$";
			pattern = Pattern.compile(regex);
		}
		
		Matcher matcher = pattern.matcher(note);
		if (!matcher.matches()) {
			return null;
		}
		
		return new Note(matcher.group("letter").toUpperCase(), matcher.group("accidental"));
	}
	
	protected static int calculateSemitones(String accidental) {
		switch(accidental) {
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
	
	protected static String calculateAccidental(int semitones) {
		switch(semitones) {
		case 1:
			return "#";
		case 2:
			return "x";
		case -1:
			return "b";
		case -2:
			return "bb";
		default:
			return "";
		}
	}
	
	/**
	 * Returns the note with the same letter name obtained by adding the given number of semitones.
	 * @param semitones the number of semitones to change the note by
	 * @return the new note, or null if the note would go beyond 3 flats or 3 sharps
	 */
	private Note changeAccidental(int semitones) {
		// calculate the required semitone alteration
		int newSemitones = semitoneChange + semitones;
		
		// don't calculate notes greater than 3 flats or 3 flats
		if (Math.abs(newSemitones) > 3) {
			return null;
		}

		return Note.getNote(letter + Note.calculateAccidental(newSemitones));
	}
	
	/**
	 * Finds the note obtained by applying the given interval to the current note.
	 * @param interval the interval to apply
	 * @return the new note, or null if more than 3 sharps/flats would be required to create the new note
	 */
	public Note addInterval(Interval interval) {
		// calculate the required number of semitones to increase the note by
		int requiredSemitones = Interval.intervalToSemitones(interval);
		
		// find the position of the current note in the array of letter names, then add the interval
		// to find the letter name of the second note
		String letterName = null;
		for (int i = 0; i < LETTER_NAMES.length; i++) {
			if (letter.equals(LETTER_NAMES[i])) {
				letterName = LETTER_NAMES[(i + interval.simplifyNumber() - 1) % LETTER_NAMES.length];
				break;
			}
		}
		
		// get the new note (without any accidentals)
		Note newNote = Note.getNote(letterName);
		
		// calculate how many semitones are still required and apply an accidental to account for the difference
		int currentSemitones = Interval.countSemitones(this, newNote);
		return newNote.changeAccidental(requiredSemitones - currentSemitones);
	}
	
	public String toString() {
		return letter + accidental;
	}
}
