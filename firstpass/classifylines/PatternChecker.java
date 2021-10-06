package oop.ex6.firstpass.classifylines;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * this class checks patterns of lines against their kind.
 */
public class PatternChecker implements PatternCheck {
	// a hashmap from all the LineKinds to their Patterns
	private static final HashMap<LineKind, Pattern> lineKindToPattern
			= new HashMap<LineKind, Pattern>() {{
		for (LineKind lineKind : LineKind.values()) {
			put(lineKind, Pattern.compile(lineKind.patternRegex()));
		}
	}};

	/**
	 * this method checks if a certain line fits a certain kind's pattern.
	 * @param line line to check.
	 * @param kind kind of line to check pattern of.
	 * @return true iff the line fits the kind's pattern.
	 */
	@Override
	public boolean checkPattern(String line, LineKind kind) {
		return lineKindToPattern.get(kind).matcher(line).matches();
	}
}
