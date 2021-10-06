package sjavac.firstpass.classifylines;

/**
 * an interface that identify lines.
 */
public interface Identifier {

	/**
	 * this method is returning the lind of a certain line.
	 * @param line line to identify
	 * @return LineKind Object.
	 * @throws NoIdendtiferMatch thrown if line does not match any of the kinds.
	 */
	LineKind identify(String line) throws NoIdendtiferMatch;
}
