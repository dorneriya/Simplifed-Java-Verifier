package sjavac.secondPass;

/**
 * thrown in case a function was ended without 'return' statement.
 */
public class NoReturnEndOfFunctionException extends FileLogicException {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor.
	 */
	NoReturnEndOfFunctionException() {
		this.errorMsg = "function did not end with 'return' statement.";
	}
}
