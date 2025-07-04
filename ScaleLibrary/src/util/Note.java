package util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a musical note.
 * @author Joel Gibson
 */
public class Note {
	/**
	 * The letter name of the note.
	 */
	private String letter;
	
	/**
	 * The accidental applied to the note.
	 */
	private Alteration accidental;
	
	/**
	 * The sequence of possible letter names.
	 */
	public static final String[] LETTER_NAMES = {"A", "B", "C", "D", "E", "F", "G"};
	
	/**
	 * The regex pattern for parsing note names.
	 */
	private static Pattern pattern;
	
	/**
	 * Maps note names to the associated Note object.
	 */
	protected static Map<String, Note> notes = new HashMap<>();
	
	/**
	 * Creates a new Note with the given letter name and accidental.
	 * @param letter the letter name
	 * @param accidental the accidental
	 */
	protected Note(String letter, String accidental) {
		this.letter = letter;
		this.accidental = Alteration.getAlteration(accidental);
	}
	
	/**
	 * @return the letter name
	 */
	public String getLetter() {
		return letter;
	}

	/**
	 * @return the accidental
	 */
	public Alteration getAccidental() {
		return accidental;
	}
	
	/**
	 * @return the number of semitones the accidental changes the letter name by.
	 */
	public int getSemitoneChange() {
		return accidental.getSemitoneChange();
	}

	/**
	 * Static factory method for creating/retrieving the Note object associated with the given name.
	 * @param note the name of the note
	 * @return the Note object, of null if note name was invalid
	 */
	public static Note getNote(String noteName) {
		noteName = noteName.toLowerCase();
		
		// get note if it has been created previously
		if (notes.containsKey(noteName)) {
			return notes.get(noteName);
		}
		
		// create new note
		Note note = Note.parseNote(noteName);
		if (note != null) {
			notes.put(noteName, note);
		}
		return note;
	}
	
	/**
	 * Creates a note object using the given note name.
	 * @param noteName the note name
	 * @return the Note object, or null if note name was invalid
	 */
	protected static Note parseNote(String noteName) {
		// create regex pattern for extracting the note components
		if (pattern == null) {
			String regex = "^(?<letter>[a-gA-G])(?<accidental>[#bx]?|bb)$";
			pattern = Pattern.compile(regex);
		}
		
		Matcher matcher = pattern.matcher(noteName);
		if (!matcher.matches()) {
			return null;
		}
		
		return new Note(matcher.group("letter").toUpperCase(), matcher.group("accidental"));
	}
	
	/**
	 * Returns the note with the same letter name obtained by adding the given number of semitones.
	 * @param semitones the number of semitones to change the note by
	 * @return the new note, or null if the new note requires more than 2 sharps/flats
	 */
	protected Note changeAccidental(int semitones) {
		// calculate the required semitone alteration
		int newSemitones = accidental.getSemitoneChange() + semitones;
		
		// don't calculate notes greater than 2 flats or sharps
		if (Math.abs(newSemitones) > 2) {
			return null;
		}

		return Note.getNote(letter + Alteration.getAlteration(newSemitones));
	}
	
	/**
	 * Finds the note obtained by applying the given interval to the current note.
	 * @param interval the interval to apply
	 * @return the new note, or null if more than 2 sharps/flats would be required to create the new note
	 */
	public Note addInterval(Interval interval) {
		// calculate the required number of semitones to increase the note by
		int requiredSemitones = Interval.intervalToSemitones(interval);
		
		// find the position of the current note in the array of letter names, then add the interval
		// to find the letter name of the second note
		String letterName = null;
		for (int i = 0; i < LETTER_NAMES.length; i++) {
			if (letter.equals(LETTER_NAMES[i])) {
				letterName = LETTER_NAMES[(i + interval.simplifySize() - 1) % LETTER_NAMES.length];
				break;
			}
		}
		
		// get the new note (without any accidentals)
		Note newNote = Note.getNote(letterName);
		
		// calculate how many semitones are still required and apply an accidental to account for the difference
		int currentSemitones = Interval.countSemitones(this, newNote);
		return newNote.changeAccidental(requiredSemitones - currentSemitones);
	}
	
	@Override
	public String toString() {
		return letter + accidental;
	}
	
	@Override
	public boolean equals(Object o) {
		Note otherNote = (Note) o;
		return this.toString().equals(otherNote.toString());
	}
}
