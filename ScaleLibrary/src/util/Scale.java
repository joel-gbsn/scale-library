package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scale {
	
	// structure for memoization
	Map<String, List<Note>> scales = new HashMap<String, List<Note>>();
	
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
	
	public List<Note> getScale(String root) {
		root = root.substring(0, 1).toUpperCase() + root.substring(1);
		
		if (scales.containsKey(root)) {
			return scales.get(root);
		}
		
		List<Note> newScale = generateScale(root);
		if (newScale != null) {
			scales.put(root, newScale);
		}
		return newScale;
	}
	
	private List<Note> generateScale(String root) {
		List<Note> scale = new ArrayList<>();
		
		return scale;
	}
}
