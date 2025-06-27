package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scale {
	
	// structure for memoization
	Map<String, List<Note>> scales = new HashMap<>();
	
	// e.g. melodic minor
	String name;
	
	public List<Interval> intervals;
	
	boolean simplify;
	
	public Scale(String name, List<Interval> intervals, boolean simplify) {
		this.name = name;
		this.intervals = intervals;
		this.simplify = simplify;
	}
	
	public List<Note> getScale(Note root) {
		if (scales.containsKey(root.toString())) {
			return scales.get(root.toString());
		}
		
		List<Note> newScale = generateScale(root);
		if (newScale != null) {
			scales.put(root.toString(), newScale);
		}
		return newScale;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Note> generateScale(Note root) {
		List<Note> notes = new ArrayList<>();
		
		for (Interval interval : intervals) {
			Note nextNote = root.addInterval(interval);
			notes.add(nextNote);
		}
		
		if (notes.contains(null)) {
			return null;
		}
		
		if (simplify) {
			Scale.shuffleAccidentals(notes, root);
		}
		
		if ("blues".equals(name)) {
			Scale.shuffleAccidentals(notes, root, 3);
		}
		
		return notes;
	}
	
	/**
	 * Simplifies the note at the given index in the scale so that the least number of accidentals are used.
	 * @param index the index of the note to change
	 */
	static void shuffleAccidentals(List<Note> notes, Note root, int index) {
		// find the note at the required index and its current semitone change
		Note note = notes.get(index);
		int semitones = note.accidental.getSemitoneChange();

		// check if the enharhomic note above or below the current note has a smaller semitone change
		String[] intervals = {"#7", "bb2"};
		for (String interval : intervals) {

			// apply the interval to find the test note
			Note testNote = note.addInterval(Interval.getInterval(interval));
			if (testNote == null) {
				continue;
			}

			// use the note with the smaller semitone alteration
			int testSemitones = testNote.accidental.getSemitoneChange();
			if (Math.abs(testSemitones) < Math.abs(semitones)) {
				notes.set(index, testNote);
			} else if (Math.abs(testSemitones) > Math.abs(semitones)) {
				continue;
			}

			// use the note with the more similar semitone alteration to the root note
			int rootSemitones = root.accidental.getSemitoneChange();
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
	
	/**
	 * Simplifies each non-root note in the scale so that the least number of accidentals are used.
	 */
	static void shuffleAccidentals(List<Note> notes, Note root) {
		for (int i = 0; i < notes.size(); i++) {
			// check that the current note isn't the root
			Note nextNote = notes.get(i);
			if (nextNote != null && !root.equals(nextNote)) {
				shuffleAccidentals(notes, root, i);
			}
		}
	}
	
	public String convertIntervalsToString() {
		List<String> intervalList = new ArrayList<>();
		for (Interval interval : intervals) {
			intervalList.add(interval.toString());
		}
		return String.join(", ", intervalList);
	}
	
	public String toFileLine() {
		return name + "; " + convertIntervalsToString() + "; " + simplify;
	}
}
