package oop.ex6.firstpass;

/**
 * thrown in case a scope was not closed.
 */
public class ScopeNotClosedException extends FileStructureException {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor.
	 */
	ScopeNotClosedException() {
		this.errorMsg = "Scope not closed.";
		this.line = "";
	}
}
