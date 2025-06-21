package processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import datamanagement.Reader;
import util.Interval;
import util.Note;
import util.Scale;

public class Processor {
	
	private Reader reader;
	private List<Scale> scales;
	
	private Scale currentScale;
	
	public Processor(Reader reader) {
		this.reader = reader;
		this.scales = reader.readScales();
	}
	
	public List<String> getScaleNames() {
		List<String> scaleNames = new ArrayList<>();
		for (Scale scale : scales) {
			scaleNames.add(scale.getName());
		}
		return scaleNames;
	}
	
	public void setCurrentScale(int index) {
		currentScale = scales.get(index);
	}
	
	public String getScaleName() {
		return currentScale.getName();
	}
	
	public String getIntervalSequence() {
		Interval[] intervals = currentScale.intervals;
		String output = intervals[0].toString();
		for (int i = 1; i < intervals.length; i++) {
			output = output + ", " + intervals[i].toString();
		}
		return output;
	}
	
	public String getNoteSequence(String root) {
		List<Note> notes = currentScale.getScale(root);
		String output = notes.get(0).toString();
		for (int i = 1; i < notes.size(); i++) {
			output = output + ", " + notes.get(i).toString();
		}
		return output;
	}

}
