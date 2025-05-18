package util;

import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class Interval {
	private String symbol;
	private int size;
	private int semitoneChange;
	
	private static Pattern pattern;
	
	private static Map<String, Interval> intervals = new HashMap<>();
	
	protected Interval(String symbol, int size, int semitoneChange) {
		this.symbol = symbol;
		this.size = size;
		this.semitoneChange = semitoneChange;
	}
	
	public static Interval getInterval(String interval) {
		if (intervals.containsKey(interval)) {
			return intervals.get(interval);
		}
		
		Interval newInterval = Interval.parseInterval(interval);
		if (newInterval != null) {
			intervals.put(interval, newInterval);
		}
		return newInterval;
	}
	
	protected static Interval parseInterval(String interval) {
		if (pattern == null) {
			String regex = "^(?<quality>[#bx]?|bb)(?<size>[0-9]{1,2})$";
			pattern = Pattern.compile(regex);
		}
		
		Matcher matcher = pattern.matcher(interval);
		if (!matcher.matches()) {
			return null;
		}
		
		int size = Integer.parseInt(matcher.group("size"));
		if (size == 0 || size > 15) {
			return null;
		}
		
		return new Interval(interval, size, calculateSemitones(matcher.group("quality")));
	}
	
	protected static int calculateSemitones(String quality) {
		switch(quality) {
		case "#":
			return 1;
		case "x":
			return 2;
		case "b":
			return -1;
		case "bb":
			return -2;
		default:
			return 0;
		}
	}
}
