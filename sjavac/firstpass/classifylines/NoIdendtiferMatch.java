package sjavac.firstpass.classifylines;

import sjavac.firstpass.FileStructureException;

/**
 * an exception thrown in case a line could not be identified for it's kind.
 */
public class NoIdendtiferMatch extends FileStructureException {
	private static final long serialVersionUID = 1L;

	/**
	 * a constructor
	 * @param line the line we couldn't find a match to.
	 */
	NoIdendtiferMatch(String line) {
		this.line = line;
		this.errorMsg = "this line does not fit to any legal line kind.";
	}
}
