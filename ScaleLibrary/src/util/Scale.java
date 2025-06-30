package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a musical scale type.
 * @author Joel Gibson
 */
public class Scale {
	/**
	 * Maps root note names to note sequences already created for this scale type.
	 */
	private Map<String, List<Note>> scales = new HashMap<>();
	
	/**
	 * The scale name/type (e.g. major).
	 */
	private String name;
	
	/**
	 * The interval pattern of the scale.
	 */
	private List<Interval> intervals;
	
	/**
	 * Indicates whether the scale notes should be simplified using enharmonics.
	 */
	private boolean simplified;
	
	/**
	 * Creates a new scale with the given parameters.
	 * @param name the scale name
	 * @param intervals the list of intervals of the scale
	 * @param simplified whether the scale should be simplified using enharmonics
	 */
	public Scale(String name, List<Interval> intervals, boolean simplified) {
		this.name = name;
		this.intervals = intervals;
		this.simplified = simplified;
	}
	
	/**
	 * @return the scale name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the list of intervals as strings
	 */
	public List<String> getIntervalNames() {
		List<String> intervalNames = new ArrayList<>();
		for (Interval interval : intervals) {
			intervalNames.add(interval.toString());
		}
		return intervalNames;
	}

	/**
	 * @return the simplify
	 */
	public boolean isSimplified() {
		return simplified;
	}
	
	/**
	 * Returns the note sequence for this scale with the given root note.
	 * @param root the root note of the scale
	 * @return the list of notes in the scale
	 */
	public List<Note> getScale(Note root) {
		// check if scale has been created before
		if (scales.containsKey(root.toString())) {
			return scales.get(root.toString());
		}
		
		// create the scale using the root
		List<Note> newScale = generateScale(root);
		if (newScale != null) {
			scales.put(root.toString(), newScale);
		}
		return newScale;
	}

	/**
	 * Creates note sequence for this scale with the given root note.
	 * @param root the root note to create the scale above
	 * @return the list of notes in the scale
	 */
	protected List<Note> generateScale(Note root) {
		List<Note> notes = new ArrayList<>();
		
		// create each note using the interval pattern of the scale
		for (Interval interval : intervals) {
			Note nextNote = root.addInterval(interval);
			notes.add(nextNote);
		}
		
		// check that all notes were created correctly (note requiring more than 2 sharps or flats
		// will not be created)
		if (notes.contains(null)) {
			return null;
		}
		
		// use enharmonics to simplify complex accidentals
		if (simplified) {
			simplifyAccidentals(notes, root);
		}
		
		// only the flat 5th of the blues scales should be simplified
		if ("blues".equals(name)) {
			simplifyAccidentals(notes, root, 3);
		}
		
		return notes;
	}
	
	/**
	 * Simplifies each non-root note in the scale so that the least number of accidentals are used.
	 * @param notes the list of notes in the scale
	 * @param root the root note of the scale
	 */
	protected void simplifyAccidentals(List<Note> notes, Note root) {
		for (int i = 0; i < notes.size(); i++) {
			Note nextNote = notes.get(i);
			// only simplify non-root notes
			if (nextNote != null && !root.equals(nextNote)) {
				simplifyAccidentals(notes, root, i);
			}
		}
	}
	
	/**
	 * Simplifies the scale note at the given index so that the least number of accidentals are used.
	 * @param notes the list of notes in the scale
	 * @param root the root note of the scale
	 * @param index the index of the note to change
	 */
	protected void simplifyAccidentals(List<Note> notes, Note root, int index) {
		// find the note at the required index and its current semitone change
		Note note = notes.get(index);
		int semitones = note.getSemitoneChange();

		// check if the enharhomic note above or below the current note has a smaller semitone change
		String[] intervals = {"#7", "bb2"};
		for (String interval : intervals) {

			// apply the interval to find the test note
			Note testNote = note.addInterval(Interval.getInterval(interval));
			if (testNote == null) {
				continue;
			}

			// use the note with the smaller semitone alteration
			int testSemitones = testNote.getSemitoneChange();
			if (Math.abs(testSemitones) < Math.abs(semitones)) {
				notes.set(index, testNote);
			} else if (Math.abs(testSemitones) > Math.abs(semitones)) {
				continue;
			}

			// use the note with the more similar semitone alteration to the root note
			int rootSemitones = root.getSemitoneChange();
			if (Math.abs(rootSemitones - testSemitones) < Math.abs(rootSemitones - semitones)) {
				notes.set(index, testNote);
			} else if (Math.abs(rootSemitones - testSemitones) > Math.abs(rootSemitones - semitones)) {
				continue;
			}

			// use the note spelled with a sharp rather than a flat
			if (testSemitones > semitones) {
				notes.set(index, testNote);
			}
		}
	}
}
