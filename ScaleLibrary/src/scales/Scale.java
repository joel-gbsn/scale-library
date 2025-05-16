package scales;

import java.util.ArrayList;

import notes.*;

/**
 * Represents an sequence of musical notes, such as a scale, mode or arpeggio.
 * @author Joel Gibson
 */
public class Scale {
	
	/**
	 * The root note of the scale.
	 */
    private Note root;
	
    /**
     * The type/name of the scale (e.g. major, minor).
     */
	private String type;
	
	/**
	 * The format of the scale (e.g. scale, arpeggio).
	 */
	private String format;
	
	/**
	 * The sequence of intervals used to construct the scale.
	 */
	private String[] intervalPattern;
	
	/**
	 * The list of notes in the scale.
	 */
	private ArrayList<Note> notes;
	
	/**
	 * Creates a scale based on the given root note.
	 * @param root the root note
	 * @param format the format of the scale (e.g. scale, arpeggio)
	 * @param type the type of the scale (e.g. major, minor)
	 * @param intervals the sequence of intervals for each note in the scale
	 */
	public Scale(Note root, String format, String type, String[] intervals) {
		this.root = root;
		this.format = format;
		this.type = type;
		this.intervalPattern = intervals;
		
		// calculate all notes in the scale
		this.createSequence();
	}
	
	/**
	 * Gets the root note.
	 * @return the root note
	 */
	public Note getRoot() {
		return root;
	}
	
	/**
	 * Gets the scale format.
	 * @return the scale format
	 */
	public String getFormat() {
		return format;
	}
	
	/**
	 * Gets the scale type.
	 * @return the scale type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Gets the list of notes in the scale.
	 * @return the list of notes
	 */
	public ArrayList<Note> getNotes() {
		return notes;
	}
	
	/**
	 * Creates every note of the scale using the interval pattern.
	 */
	private void createSequence() {
		// the list to store each note
		notes = new ArrayList<Note>();
		
		// create the notes by applying each interval to the root note
		for (String interval : intervalPattern) {
			Note nextNote = root.addInterval(Interval.getInterval(interval));
			notes.add(nextNote);
		}
	}
	
	/**
	 * Prints the list of notes in the scale on a single line.
	 */
	public void printNotes() {
		// prints the scale name
		System.out.println(this);
		
		// print each note using a window of 5 spaces per note
		for (Note note : notes.subList(0, notes.size() - 1)) {
			System.out.print(note);
			
			// print blank spaces to pad
			for (int i = 0; i < 5 - note.toString().length(); i++) {
				System.out.print(" ");
			}
			
		}
		
		// print the final note
		System.out.println(notes.get(notes.size() - 1));
		System.out.println();
	}
	
	/**
	 * Simplifies the note at the given index in the scale so that the least number of accidentals are used.
	 * @param index the index of the note to change
	 */
	void shuffleAccidentals(int index) {
		// get the list of notes in the scale
		ArrayList<Note> notes = getNotes();
		
		// find the note at the required index and its current semitone alteration
		Note note = notes.get(index);
		int semitones = note.getAccidental().getSemitones();
		
		// check if the enharhomic note above or below the current note has fewer accidentals
		String[] intervals = {"#7", "bb2"};
		for (String interval : intervals) {
			
			// apply the interval to find the test note
			Note testNote = note.addInterval(Interval.getInterval(interval));
			if (testNote != null) {
				// calculate the semitone alteration of the new note
				int testSemitones = testNote.getAccidental().getSemitones();
				
				// compare the semitone changes of the current and test notes
				if (Math.abs(testSemitones) < Math.abs(semitones)) {
					// use the new note if it contains a smaller semitone change than the original note
					notes.set(index, testNote);
					
				} else if (Math.abs(testSemitones) == Math.abs(semitones)) {
					// find the semitone alternation of the root note
					int rootSemitones = getRoot().getAccidental().getSemitones();
					
					if (Math.abs(rootSemitones - testSemitones) < Math.abs(rootSemitones - semitones)) {
						// if the semitone alterations on the original and test notes are equal, use the note
						// with the accidental most similar to the accidental on the root note of the scale
						notes.set(index, testNote);
						
					} else {
						// if the original and test notes are still not differentiated, use the note spelled
						// with a sharp rather than a flat
						if (testSemitones > semitones) {
							notes.set(index, testNote);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Simplifies each non-root note in the scale so that the least number of accidentals are used.
	 */
	void shuffleAccidentals() {
		for (int i = 0; i < getNotes().size(); i++) {
			// check that the current note isn't the root
			Note nextNote = getNotes().get(i);
			if (nextNote != null && !getRoot().equals(nextNote)) {
				shuffleAccidentals(i);
			}
		}
	}
	
	/**
	 * Checks that each note of the scale has been successfully created and does not exceed
	 * a double sharp or double flat
	 * @return true if all notes are valid, or false if at least one note is null
	 */
	boolean isValid() {
		for (Note note : getNotes()) {
			if (note == null || Math.abs(note.getAccidental().getSemitones()) > 2) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns a string containing the full name of the scale
	 */
	@Override
	public String toString() {
		return getRoot() + " " + getType() + " " + getFormat();
	}
}
