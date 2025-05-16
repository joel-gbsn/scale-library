package util;

public class Scale {
	
	// structure for memoization
	// map
	
	// e.g. melodic minor
	String name;
	
	// e.g. ascending
	String subtype;
	
	// e.g. scale
	String format;
	
	Interval[] intervals;
	
	public Scale(String name, String subtype, String format, Interval[] intervals) {
		this.name = name;
		this.subtype = subtype;
		this.format = format;
		this.intervals = intervals;
	}
	
}
