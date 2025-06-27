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
	
	private String currScaleSet;
	private Scale currScale;
	
	public Processor(Reader reader, Writer writer) {
		scaleSets.put("base", reader.readBaseScales());
		scaleSets.put("custom", reader.readCustomScales());
		this.writer = writer;
	}
	
	public String getCurrScaleSet() {
		return currScaleSet;
	}
	
	public void useScaleSet(String scaleSet) {
		currScaleSet = scaleSet;
	}
	
	public void addCustomScale(String name, List<Interval> intervals, boolean simplify) {
		scaleSets.get("custom").add(new Scale(name, intervals, simplify));
		writeCustomScales();
	}
	
	public void deleteCustomScale(int index) {
		scaleSets.get("custom").remove(index);
		writeCustomScales();
	}
	
	public List<String> getScaleNames() {
		List<String> scaleNames = new ArrayList<>();
		for (Scale scale : scaleSets.get(currScaleSet)) {
			scaleNames.add(scale.getName());
		}
		return scaleNames;
	}
	
	public void setScale(int index) {
		currScale = scaleSets.get(currScaleSet).get(index);
	}
	
	public String getScaleName() {
		return currScale.getName();
	}
	
	public String getIntervalString() {
		return currScale.convertIntervalsToString();
	}
	
	public List<String> createScale(Note root) {
		List<Note> notes = currScale.getScale(root);
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

}
