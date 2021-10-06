package oop.ex6.firstpass;

import oop.ex6.main.Ex6Exception;

/**
 * thrown in case there was a problem with the file's structure.
 */
public class FileStructureException extends Ex6Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * a default constructor (should not be used, in general).
	 */
	public FileStructureException() {
		this.errorMsg =
				"Exception not initialized correctly. probably thrown without being catched by " +
				"FirstPasser.";
	}

	/**
	 * constructs an exception from another exception with lune and line number
	 * @param exception the other exception
	 * @param line the line in which the exception occurred.
	 * @param lineNum the line number which the exception occurred.
	 */
	public FileStructureException(FileStructureException exception, String line, int lineNum) {
		this.errorMsg = exception.errorMsg;
		this.lineNum = lineNum;
		this.line = line;
		if (line == null) {
			this.line = "";
		}
	}

}
