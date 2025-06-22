package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scale {
	
	// structure for memoization
	Map<String, List<Note>> scales = new HashMap<>();
	
	// e.g. melodic minor
	String name;
	
	public Interval[] intervals;
	
	boolean simplify;
	
	public Scale(String name, Interval[] intervals, boolean simplify) {
		this.name = name;
		this.intervals = intervals;
		this.simplify = simplify;
	}
	
	public List<Note> getScale(Note root) {
		if (scales.containsKey(root.toString())) {
			return scales.get(root.toString());
		}
		
		List<Note> newScale = generateScale(root);
		if (newScale != null) {
			scales.put(root.toString(), newScale);
		}
		return newScale;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Note> generateScale(Note root) {
		List<Note> scale = new ArrayList<>();
		
		return scale;
	}
}
