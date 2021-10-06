package oop.ex6.firstpass.classifylines;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a class implementing the Identifier Interface.
 */
public class IdentifyLine implements Identifier {
	// a hashMap from lineKinds to their Identifier Pattern
	private static final HashMap<LineKind, Pattern> lineKindToIdentifier
			= new HashMap<LineKind, Pattern>() {{
		for (LineKind lineKind : LineKind.values()) {
			put(lineKind, Pattern.compile(lineKind.identifierRegex()));
		}
	}};

	/**
	 * this method is returning the lind of a certain line.
	 * @param line line to identify
	 * @return LineKind Object.
	 * @throws NoIdendtiferMatch thrown if line does not match any of the kinds.
	 */
	@Override
	public LineKind identify(String line) throws NoIdendtiferMatch {
		for (Map.Entry<LineKind, Pattern> entry : lineKindToIdentifier.entrySet()) {
			Matcher m = entry.getValue().matcher(line); // try to match the identifier
			if (m.find()) {
				return entry.getKey(); // update the cur line
			}
		}
		throw new NoIdendtiferMatch(line);
	}
}
