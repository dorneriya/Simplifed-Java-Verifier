package oop.ex6.secondPass;

/**
 * thrown in case an invalid condition was given to a condition scope (i.e if statement)
 */
public class InvalidConditionException extends FileLogicException {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * @param arg the argument given as condition.
	 */
	InvalidConditionException(String arg) {
		this.errorMsg = String.format("Arg %s is not a valid condition", arg);
	}
}
