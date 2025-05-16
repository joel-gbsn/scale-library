package notes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a music note.
 * @author Joel Gibson
 */
public class Note {
	
	/**
	 * The letter name of the note.
	 */
	private String letterName;
	
	/**
	 * The accidental applied to the note.
	 */
	private Accidental accidental;
	
	/**
	 * The array of all possible note letter names.
	 */
	public static final String[] LETTER_NAMES = {"A", "B", "C", "D", "E", "F", "G"};
	
	/**
	 * A mapping of all note names to their associated Note objects.
	 */
	private static Map<String, Note> notes = new HashMap<String, Note>();
	
	/**
	 * The list of all root notes used to construct scales.
	 */
	private static ArrayList<Note> rootNotes = new ArrayList<Note>();
	
	/**
	 * Creates a note from the given letter name and accidental.
	 * @param letterName the base letter name of the note
	 * @param accidental the accidental applied to the note
	 */
	public Note(String letterName, Accidental accidental) {
		this.letterName = letterName;
		this.accidental = accidental;
		
		// store the note using its string representation
		notes.put(letterName.toLowerCase() + accidental, this);
	}
	
	/**
	 * Gets the letter name of the note.
	 * @return the letter name
	 */
	public String getLetterName() {
		return letterName;
	}

	/**
	 * Gets the accidental applied to the note.
	 * @return the accidental
	 */
	public Accidental getAccidental() {
		return accidental;
	}
	
	/**
	 * Gets the map containing all notes.
	 * @return the map of notes
	 */
	public static Map<String, Note> getNotes() {
		return notes;
	}
	
	/**
	 * Gets the list of root notes that can be used to construct scales.
	 * @return the list of root notes
	 */
	public static ArrayList<Note> getRootNotes() {
		return rootNotes;
	}
	
	/**
	 * Creates a list of all possible notes between 3 flats and 3 sharps.
	 */
	public static void createAllNotes() {
		// create each note using every letter name and accidental
		Note note;
		for (String letter : LETTER_NAMES) {
			for (Accidental accidental : Accidental.getAccidentals()) {
				note = new Note(letter, accidental);
				
				// only notes between 1 flat and 1 sharp will be used as root notes for creating scales
				if (Math.abs(accidental.getSemitones()) <= 1) {
					rootNotes.add(note);
				}
			}
		}
	}
	
	/**
	 * Returns the Note object associated with the given string representation of the note.
	 * @param noteName the name of the note (e.g. F, Eb or C#)
	 * @return the Note object
	 */
	public static Note getNote(String noteName) {
		return notes.get(noteName.toLowerCase());
	}
	
	/**
	 * Returns the note with the same letter name obtained by adding the given number of semitones.
	 * @param semitones the number of semitones to change the note by
	 * @return the new note, or null if the note would go beyond 3 flats or 3 sharps
	 */
	private Note changeAccidental(int semitones) {
		// find the current semitone alteration to the note
		int currentSemitones = this.getAccidental().getSemitones();
		
		// calculate the required semitone alteration
		int newSemitones = currentSemitones + semitones;
		
		// don't calculate notes greater than 3 flats or 3 flats
		if (Math.abs(newSemitones) > 3) {
			return null;
		}

		// get the new note using its string representation
        String newNoteName = getLetterName() + Accidental.convertSemitones(newSemitones).getSymbol();
		return getNote(newNoteName);
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
			if (getLetterName().equals(LETTER_NAMES[i])) {
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

	/**
	 * Returns a string representation of the note for printing.
	 */
	@Override
	public String toString() {
		return getLetterName() + getAccidental();
	}
	
	/**
	 * Compares the notes for equality using their letter names and accidentals.
	 */
	@Override
	public boolean equals(Object note) {
		Note otherNote = (Note) note;
		
		return (this.getLetterName().equals(otherNote.getLetterName()) &&
				this.getAccidental().equals(otherNote.getAccidental()));
	}
}
