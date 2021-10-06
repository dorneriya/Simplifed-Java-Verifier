package oop.ex6.secondPass;

/**
 * thrown in case of an invalid assignment to variable.
 */
public class InvalidAssignException extends FileLogicException {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * @param var the variable to assign to
	 * @param val the value to assign to.
	 */
	public InvalidAssignException(String var, String val) {
		this.errorMsg = String.format("cannot assign value %s to variable %s", val, var);
	}
}
