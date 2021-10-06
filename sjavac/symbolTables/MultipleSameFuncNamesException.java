package sjavac.symbolTables;

import sjavac.secondPass.FileLogicException;

/**
 * thrown in case a function we try to declare has already been declared.
 */
public class MultipleSameFuncNamesException extends FileLogicException {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor.
	 * @param funcName the function name.
	 */
	MultipleSameFuncNamesException(String funcName) {
		this.errorMsg = String.format("finction %s is already declared in this file", funcName);
	}
}
