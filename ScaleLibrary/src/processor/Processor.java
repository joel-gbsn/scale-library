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

public class Processor {
	
	private Writer writer;
	
	private Map<String, List<Scale>> scaleSets = new HashMap<>();
	
	public Processor(Reader reader, Writer writer) {
		scaleSets.put("base", reader.readBaseScales());
		scaleSets.put("custom", reader.readCustomScales());
		this.writer = writer;
	}
	
	public void addCustomScale(String name, List<Interval> intervals, boolean simplify) {
		scaleSets.get("custom").add(new Scale(name, intervals, simplify));
		writeCustomScales();
	}
	
	public void deleteCustomScale(int index) {
		scaleSets.get("custom").remove(index);
		writeCustomScales();
	}
	
	public List<String> getScaleNames(String scaleSet) {
		List<String> scaleNames = new ArrayList<>();
		for (Scale scale : scaleSets.get(scaleSet)) {
			scaleNames.add(scale.getName());
		}
		return scaleNames;
	}
	
	public String getIntervals(String scaleSet, int index) {
		return getScale(scaleSet, index).getIntervalString();
	}
	
	public List<String> getScaleNotes(String scaleSet, int index, Note root) {
		Scale scale = getScale(scaleSet, index);
		List<Note> notes = scale.getScale(root);
		if (notes == null) {
			return null;
		}
		
		List<String> output = new ArrayList<>();
		for (Note note : notes) {
			output.add(note.toString());
		}
		return output;
	}
	
	public void writeCustomScales() {
		writer.writeScales(scaleSets.get("custom"));
	}
	
	private Scale getScale(String scaleSet, int index) {
		return scaleSets.get(scaleSet).get(index);
	}

}
