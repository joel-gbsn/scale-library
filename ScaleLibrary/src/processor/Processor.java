package processor;

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

}
