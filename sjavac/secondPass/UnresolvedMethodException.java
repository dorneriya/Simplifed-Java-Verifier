package sjavac.secondPass;

/**
 * thrown in case a method that wasn't defined is called.
 */
public class UnresolvedMethodException extends FileLogicException {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * @param name the method's name.
	 */
	public UnresolvedMethodException(String name) {
		this.errorMsg = String.format("cannot resolve method '%s'", name);
	}
}
