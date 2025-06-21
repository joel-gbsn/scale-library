package processor;

import java.util.ArrayList;
import java.util.List;

import datamanagement.Reader;
import util.Scale;

public class Processor {
	
	private Reader reader;
	private List<Scale> scales;
	
	public Processor(Reader reader) {
		this.reader = reader;
		this.scales = reader.readScales();
	}
	
	public List<String> getScaleNames() {
		List<String> scaleNames = new ArrayList<>();
		for (Scale scale : scales) {
			if (!scaleNames.contains(scale.getName())) {
				scaleNames.add(scale.getName());
			}
		}
		return scaleNames;
	}

}
