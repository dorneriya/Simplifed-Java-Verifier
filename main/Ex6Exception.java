package oop.ex6.main;

/**
 * an abstract all-project exception.
 */
public abstract class Ex6Exception extends Exception {
	private static final long serialVersionUID = 1L;
	protected String errorMsg = "thrown without message";
	protected int lineNum = -1;
	protected String line;

	/**
	 * prints out the exception's error message.
	 */
	public void printMsg() {
		System.err.printf("Error in line %d: %s %s\n", this.lineNum, this.line, this.errorMsg);
	}
}
