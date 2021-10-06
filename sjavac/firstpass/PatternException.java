package sjavac.firstpass;

import sjavac.firstpass.classifylines.LineKind;

/**
 * thrown in case a line doesn't fit it's kind's pattern.
 */
public class PatternException extends FileStructureException {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * @param kind the linekind of the line.
	 */
	PatternException(LineKind kind) {
		this.errorMsg = String.format("line does not fit to %s kind", kind);
	}
}
