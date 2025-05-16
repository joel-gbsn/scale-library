package scales;

import java.util.HashMap;
import java.util.Map;

import notes.Note;

/**
 * Represents a collection of scales of the same format and type using different root notes.
 * @author Joel Gibson
 */
public class ScaleCollection {
	
	/**
	 * The format of the scales (e.g. scale, arpeggio).
	 */
	private String format;
	
	/**
	 * The type/name of the scales (e.g. major, minor).
	 */
	private String type;
	
	/**
	 * The array of intervals used to create each note in the scales.
	 */
	private String[] intervals;
	
	/**
	 * A boolean indicating whether notes should be simplified using fewer accidentals.
	 */
	private boolean simplify;
	
	/**
	 * A mapping of all root note names to their associated Scale objects.
	 */
	private Map<String, Scale> allScales;
	
	/**
	 * Creates a scale collection using the given scale specifications.
	 * @param format the format of the scales (e.g. scale, arpeggio)
	 * @param type the type/name of the scales (e.g. major, minor)
	 * @param intervals the sequence of intervals used to construct each note in the scales
	 * @param simplify whether the notes should be simplified using a fewer number of accidentals
	 */
	public ScaleCollection(String format, String type, String[] intervals, boolean simplify) {
		this.format = format;
		this.type = type;
		this.intervals = intervals;
		this.simplify = simplify;
		
		// create a new mapping of root note names to Scale objects
		allScales = new HashMap<String, Scale>();
	}
	
	/**
	 * Get the format of the scales.
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	
	/**
	 * Get the type of the scales.
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Get the sequence of intervals used to create each note in the scales.
	 * @return the array of interval names
	 */
	public String[] getIntervals() {
		return intervals;
	}
	
	/**
	 * Get the boolean value indicating whether notes should be simplified.
	 * @return the boolean value to simplify notes
	 */
	public boolean getSimplify() {
		return simplify;
	}
	
	/**
	 * Get the map containing all root note names and their associated scales.
	 * @return the map containing all scales
	 */
	public Map<String, Scale> getAllScales() {
		return allScales;
	}

	/**
	 * Creates all scales for the collection using every possible root note.
	 */
	public void createAllScales() {
		for (Note root : Note.getRootNotes()) {
			addScale(root);
		}
	}
	
	/**
	 * Creates a scale for the current collection using the given root note.
	 * @param root the root note of the scale to create
	 */
	private void addScale(Note root) {
		// create the scale using the specification of the current collection
		Scale scale = new Scale(root, getFormat(), getType(), getIntervals());
		
		// for blues scales, the diminished 5th (note at index 3) should be simplified
		if ("blues".equals(getType()) && "scale".equals(getFormat())) {
			scale.shuffleAccidentals(3);
		}
		
		// simplify notes if required
		if (simplify) {
			scale.shuffleAccidentals();
		}
		
		// add the scale to the collection only if it is valid (i.e. all notes are non-null)
		if (scale.isValid()) {
			allScales.put(root.toString().toLowerCase(), scale);
		}
	}
	
	/**
	 * Prints the interval pattern used the create each scale in the collection.
	 */
	void printIntervals() {
		// print the name of the scale collection
		System.out.println("Interval pattern: " + this.toString());
		
		// print each interval using a window of 5 spaces per interval
		String[] intervals = getIntervals();
		for (int i = 0; i < intervals.length - 1; i++) {
			String interval = intervals[i];
            System.out.print(interval);
			
            // print blank spaces to pad
			for (int j = 0; j < 5 - interval.length(); j++) {
				System.out.print(" ");
			}	
		}
		
		// print the final interval
		System.out.println(intervals[intervals.length - 1] + "\n");
	}
	
	/**
	 * Returns a string representation of the scale collection information that can be written to a file
	 * and then read back in each time the program starts.
	 * @return the scale collection information formatted for writing
	 */
	String toFileLine() {
		return getFormat() + "; " + getType() + "; " + String.join(", ", getIntervals()) + "; " + getSimplify();
	}
	
	/**
	 * Creates a string representation of the scale collection name for printing.
	 */
	@Override
	public String toString() {
		return getType() + " " + getFormat();
	}
}
