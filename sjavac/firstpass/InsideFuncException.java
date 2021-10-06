package sjavac.firstpass;

import sjavac.firstpass.classifylines.LineKind;

/**
 * an exception thrown in case a Linekind that cannoot be inside a function was found inside a
 * function.
 */
public class InsideFuncException extends FileStructureException {
	private static final long serialVersionUID = 1L;

	/**
	 * a constructor
	 * @param kind the line kind that was found inside the function.
	 */
	InsideFuncException(LineKind kind) {
		this.errorMsg = String.format("kind %s is inside function", kind);
	}
}
