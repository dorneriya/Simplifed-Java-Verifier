package sjavac.firstpass.classifylines;

import sjavac.utils.regexConsts;

/**
 * this class is an Enum of the possible ind of line in sjava.
 */
public enum LineKind {
	VAR_DEC(regexConsts.VAR_DEC_IDENTIFIER, regexConsts.VAR_DEC_PATTERN),
	VAR_ASSIGNMENT(regexConsts.VAR_ASSIGNMENT_IDENTIFIER, regexConsts.VAR_ASSIGNMENT_PATTERN),
	FINAL_VAR_DEC(regexConsts.FINAL_VAR_DEC_IDENTIFIER, regexConsts.FINAL_VAR_DEC_PATTERN),
	FUNC_DEC(regexConsts.FUNC_DEC_IDENTIFIER, regexConsts.FUNC_DEC_PATTERN),
	FUNC_CALL(regexConsts.FUNC_CALL_IDENTIFIER, regexConsts.FUNC_CALL_PATTERN),
	IF(regexConsts.IF_IDENTIFIER, regexConsts.IF_PATTERN),
	WHILE(regexConsts.WHILE_IDENTIFIER, regexConsts.WHILE_PATTERN),
	END_SCOPE(regexConsts.END_SCOPE_IDENTIFIER, regexConsts.END_SCOPE_PATTERN),
	RETURN(regexConsts.RETURN_IDENTIFIER, regexConsts.RETURN_PATTERN),
	COMMENT(regexConsts.COMMENT_IDENTIFIER, regexConsts.COMMENT_PATTERN),
	EMPTY_LINE(regexConsts.EMPTY_LINE_IDENTIFIER, regexConsts.EMPTY_LINE_PATTERN);

	private final String identifierRegex;
	private final String patternRegex;

	/**
	 * a constructor
	 * @param identifierRegex a regex sufficient to identify a line to the specific kind.
	 * @param patternRegex a regex a line must match completely in order to be this kind.
	 */
	LineKind(String identifierRegex, String patternRegex) {
		this.identifierRegex = identifierRegex;
		this.patternRegex = patternRegex;
	}

	/**
	 * @return the LineKind's identifier regex
	 */
	public String identifierRegex() {
		return this.identifierRegex;
	}

	/**
	 * @return the LineKind's pattern regex
	 */
	public String patternRegex() {
		return this.patternRegex;
	}

}
