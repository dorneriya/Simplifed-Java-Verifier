package sjavac.secondPass;

import sjavac.main.Ex6Exception;

/**
 * thrown in case there has been a problem with the file logic.
 */
public class FileLogicException extends Ex6Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * default constructor.
	 */
	public FileLogicException() {
		this.errorMsg = "file logic exception occurred";
	}

	/**
	 * constructs an exception from another exception with lune and line number
	 * @param e the other exception
	 * @param line the line in which the exception occurred.
	 * @param lineNum the line number which the exception occurred.
	 */
	public FileLogicException(FileLogicException e, String line, int lineNum) {
		this.errorMsg = e.errorMsg;
		this.lineNum = lineNum;
		this.line = line;
	}

	/**
	 * constructs an exception from another exception with line number.
	 * @param e the other exception
	 * @param lineNum the line number which the exception occurred.
	 */
	public FileLogicException(FileLogicException e, int lineNum) {
		this.errorMsg = e.errorMsg;
		this.lineNum = lineNum;
	}
}
