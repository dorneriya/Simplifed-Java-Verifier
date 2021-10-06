package oop.ex6.firstpass;

import oop.ex6.firstpass.classifylines.LineKind;

/**
 * thrown in case a line kind that cannot be outside of function was found outside of function.
 */
public class OutsideFuncException extends FileStructureException {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * @param kind the line kind that was found outside of the function.
	 */
	OutsideFuncException(LineKind kind) {
		this.errorMsg = String.format("kind %s is outside of function\n", kind);
	}
}
