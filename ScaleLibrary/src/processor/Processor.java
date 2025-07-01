package processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datamanagement.Reader;
import datamanagement.Writer;
import util.Interval;
import util.Note;
import util.Scale;

/**
 * The processor for handling scale data.
 * @author Joel Gibson
 */
public class Processor {
	/**
	 * The map containing each scale set ("base" and "custom").
	 */
	private Map<String, List<Scale>> scaleSets = new HashMap<>();
	
	/**
	 * The writer for saving custom scales to file.
	 */
	private Writer writer;
	
	/**
	 * Creates a new processor with the given reader and writer.
	 * @param reader the reader for loading the input data
	 * @param writer the writer for saving custom scales to file
	 */
	public Processor(Reader reader, Writer writer) {
		scaleSets.put("base", reader.readBaseScales());
		scaleSets.put("custom", reader.readCustomScales());
		this.writer = writer;
	}
	
	/**
	 * Creates a new custom scale with the given parameters.
	 * @param name the scale name/type
	 * @param intervals the list of intervals in the scale
	 * @param simplify whether the notes should be simplified using enharmonics
	 */
	public void addCustomScale(String name, List<Interval> intervals, boolean simplified) {
		scaleSets.get("custom").add(new Scale(name, intervals, simplified));
		writeCustomScales();
	}
	
	/**
	 * Removes the scale at the given index from the list of custom scales.
	 * @param index the position in the list of custom scales
	 */
	public void deleteCustomScale(int index) {
		scaleSets.get("custom").remove(index);
		writeCustomScales();
	}
	
	/**
	 * Gets the list of scale names in the given scale set.
	 * @param scaleSet the scale set to search ("base" or "custom")
	 * @return the list of scale names
	 */
	public List<String> getScaleNames(String scaleSet) {
		List<String> scaleNames = new ArrayList<>();
		for (Scale scale : scaleSets.get(scaleSet)) {
			scaleNames.add(scale.getName());
		}
		return scaleNames;
	}
	
	/**
	 * Finds the list of note names in the scale with the given parameters.
	 * @param scaleSet the scale set ("base" or "custom")
	 * @param index the position of the scale in the scale set
	 * @param root the root note of the scale being searched
	 * @return the list of note names
	 */
	public List<String> getScaleNotes(String scaleSet, int index, Note root) {
		Scale scale = getScale(scaleSet, index);
		List<Note> notes = scale.getScale(root);
		
		// check if all notes could be calculated
		if (notes == null) {
			return null;
		}
		
		// convert each note to a string
		List<String> output = new ArrayList<>();
		for (Note note : notes) {
			output.add(note.toString());
		}
		return output;
	}
	
	/**
	 * Writes the list of custom scales to file.
	 */
	public void writeCustomScales() {
		writer.writeScales(scaleSets.get("custom"));
	}
	
	/**
	 * Finds the scale at the given index in the specified scale set.
	 * @param scaleSet the scale set to search
	 * @param index the position of the scale in the scale set
	 * @return the scale
	 */
	private Scale getScale(String scaleSet, int index) {
		return scaleSets.get(scaleSet).get(index);
	}
	
	/**
	 * Gets the list of interval names in the scale at the given index in the specified scale set.
	 * @param scaleSet the scale set to search
	 * @param index the position of the scale in the scale set
	 * @return the list of interval names
	 */
	public List<String> getIntervalList(String scaleSet, int index) {
		return getScale(scaleSet, index).getIntervalNames();
	}
}
