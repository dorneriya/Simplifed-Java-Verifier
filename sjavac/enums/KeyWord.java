package sjavac.enums;

/**
 * this is an enum of the keywords available in Sjavac
 */
public enum KeyWord {
	WHILE("while"),
	IF("if"),
	VOID("void"),
	FINAL("final"),
	FALSE("false"),
	TRUE("true");

	private final String stringRepresentation;

	/**
	 * constructor
	 * @param stringRepresentation the string representation of the keyword
	 */
	KeyWord(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

	/**
	 * @return the string representation of the keyword
	 */
	public String string() {
		return this.stringRepresentation;
	}
}
