package processor;

import java.util.ArrayList;
import java.util.List;

import datamanagement.Reader;
import util.Interval;
import util.Note;
import util.Scale;

public class Processor {
	private List<Scale> baseScales;
	private List<Scale> customScales;
	
	private List<Scale> currScaleSet;
	private Scale currScale;
	
	public Processor(Reader reader) {
		this.baseScales = reader.readScales();
	}
	
	public void setBaseScales() {
		currScaleSet = baseScales;
	}
	
	public void setCustomScales() {
		currScaleSet = customScales;
	}
	
	public List<String> getScaleNames() {
		List<String> scaleNames = new ArrayList<>();
		for (Scale scale : currScaleSet) {
			scaleNames.add(scale.getName());
		}
		return scaleNames;
	}
	
	public void setScale(int index) {
		currScale = currScaleSet.get(index);
	}
	
	public String getScaleName() {
		return currScale.getName();
	}
	
	public List<String> getIntervalSequence() {
		List<String> intervals = new ArrayList<>();
		for (Interval interval : currScale.intervals) {
			intervals.add(interval.toString());
		}
		return intervals;
	}
	
	public List<String> getNoteSequence(Note root) {
		List<String> notes = new ArrayList<>();
		for (Note note : currScale.getScale(root)) {
			notes.add(note.toString());
		}
		return notes;
	}

}
