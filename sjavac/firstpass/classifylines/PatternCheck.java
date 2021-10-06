package sjavac.firstpass.classifylines;

/**
 * an Interface that checks compatibility of a line to a certain LineKind.
 */
public interface PatternCheck {
	/**
	 * this method checks if a certain line fits a certain kind's pattern.
	 * @param line line to check.
	 * @param kind kind of line to check pattern of.
	 * @return true iff the line fits the kind's pattern.
	 */
	boolean checkPattern(String line, LineKind kind);
}
