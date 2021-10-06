package sjavac.firstpass;

/**
 * thrown if a line could not be identified for it's kind
 */
public class NoIdentifierFoundException extends FileStructureException {
	private static final long serialVersionUID = 1L;
	protected static String errorMsg = "line doesn't match any legal kind of line";

}
