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
	
	boolean simplify;
	
	public Scale(String format, String name, String subtype, Interval[] intervals, boolean simplify) {
		this.format = format;
		this.name = name;
		this.subtype = subtype;
		this.intervals = intervals;
		this.simplify = simplify;
	}
	
}
