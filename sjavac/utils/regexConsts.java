package sjavac.utils;

import sjavac.enums.KeyWord;
import sjavac.enums.Type;

/**
 * this class is used to define the different regex expressions needed.
 */
public class regexConsts {

	// helper REGEXES
	private static final String OPTIONAL_SPACES = "\\s*";
	private static final String MANDATORY_SPACES = "\\s+";

	// Types REGEXES
	public static final String BOOLEAN = OPTIONAL_SPACES + "(true|false)" + OPTIONAL_SPACES;
	public static final String STRING = OPTIONAL_SPACES + "\"" + ".*" + "\"" + OPTIONAL_SPACES;
	public static final String CHAR = OPTIONAL_SPACES + "'.'" + OPTIONAL_SPACES;
	public static final String INT = OPTIONAL_SPACES + "-?\\d+" + OPTIONAL_SPACES;
	public static final String DOUBLE = OPTIONAL_SPACES + "-?\\d+\\.\\d+" + OPTIONAL_SPACES;

	// REGEX for private use
	private static final String OR_TYPES = getOrTypeRegex();
			// a regex of types, e.g "int|double|boolean
	// etc.
	private static final String OR_KEYWORDS = getOrKeywordRegex();
			// a regex of Keywords, e.g "if|while etc.

	public static final String VAR_NAME_NO_SPACES =
			"(?!" + "(?:" + OR_TYPES + "|" + OR_KEYWORDS + ")" +
			"\\b)\\b" + "(_[A-Za-z]\\w*|[A-Za-z]\\w*)\\b";
	public static final String FUNC_NAME = "\\s*(?!" + "(?:" + OR_TYPES + "|" + OR_KEYWORDS + ")" +
										   ")\\b" + "([A-Za-z]\\w*)\\b\\s*";

	private static final String VAR_NAME = OPTIONAL_SPACES + VAR_NAME_NO_SPACES + OPTIONAL_SPACES;
	private static final String TYPE = OPTIONAL_SPACES + "(" + OR_TYPES + ")" + MANDATORY_SPACES;


	//Assignment Values
	private static final String NUMBER = "(" + INT + "|" + DOUBLE + ")";

	// DOR ADDITION, used to catch the value assign
	//Assignment Values
	public static final String INT_ASSIGN = OPTIONAL_SPACES + "-?\\d+" + OPTIONAL_SPACES;
	public static final String DOUBLE_ASSIGN = OPTIONAL_SPACES + "-*\\d+(\\.(\\d)+)*" +
											   OPTIONAL_SPACES;
	public static final String BOOLEAN_ASSIGN =
			OPTIONAL_SPACES + "(true|false)" + '|' + DOUBLE_ASSIGN + OPTIONAL_SPACES;
	public static final String STRING_ASSIGN = OPTIONAL_SPACES + "\".*\"" + OPTIONAL_SPACES;
	public static final String CHAR_ASSIGN = OPTIONAL_SPACES + "'.?'" + OPTIONAL_SPACES;

	public static final String REGULAR_SPACE = " ";
	public static final String COMMA = ",";
	public static final String LEFTOVERS = "[\\s;]";
	public static final String EMPTY_STRING = "";
	public static final String EQUAL = "=";
	public static final String R_BRACKET = "(";
	public static final String L_BRACKET = ")";


	private static final String AND = OPTIONAL_SPACES + "&&" + OPTIONAL_SPACES;
	private static final String OR = OPTIONAL_SPACES + "\\|\\|" + OPTIONAL_SPACES;
	public static final String LOGIC_OPERATOR =
			OPTIONAL_SPACES + "(" + AND + "|" + OR + ")" + OPTIONAL_SPACES;
	private static final String CONDITION =
			"(" + BOOLEAN + "|" + VAR_NAME + "|" + NUMBER + ")" + OPTIONAL_SPACES;
	private static final String CONDITION_LIST = CONDITION + "(" + LOGIC_OPERATOR + CONDITION +
												 ")" + "*";
	private static final String VOID = OPTIONAL_SPACES + "void" + MANDATORY_SPACES;
	private static final String IF = OPTIONAL_SPACES + "if" + OPTIONAL_SPACES;
	private static final String WHILE = OPTIONAL_SPACES + "while" + OPTIONAL_SPACES;
	private static final String RETURN = OPTIONAL_SPACES + "return" + OPTIONAL_SPACES;
	private static final String FINAL = OPTIONAL_SPACES + "final" + MANDATORY_SPACES;
	private static final String PARAMETER_DEC_LIST =
			"(" + "(" + FINAL + ")?" + TYPE + VAR_NAME + "(" + "," + "(" + FINAL + ")?" + TYPE +
			VAR_NAME + ")*" + ")?";
	private static final String ASSIGNABLE_VALUE = "(" + VAR_NAME + "|" + NUMBER +
												   "|" + BOOLEAN + "|" + STRING + "|" + CHAR + ")";
	private static final String ARGUMENTS_LIST = "(?:" + ASSIGNABLE_VALUE + "(" + "," +
												 ASSIGNABLE_VALUE + ")*" + ")*";
	private static final String LEFT_CURLY_BRACKET = OPTIONAL_SPACES + "\\{" + OPTIONAL_SPACES;
	private static final String RIGHT_CURLY_BRACKET = OPTIONAL_SPACES + "\\}" + OPTIONAL_SPACES;
	public static final String LEFT_BRACKET = OPTIONAL_SPACES + "\\(" + OPTIONAL_SPACES;
	public static final String RIGHT_BRACKET = OPTIONAL_SPACES + "\\)" + OPTIONAL_SPACES;
	private static final String COMMENT_START = "//" + OPTIONAL_SPACES;
	private static final String END_COMMAND = OPTIONAL_SPACES + ";" + OPTIONAL_SPACES;
	private static final String VAR_DEC_LIST =
			"(" + "," + VAR_NAME + "(" + "=" + ASSIGNABLE_VALUE + ")?" + ")*";

	// REGEX for public use
	public static final String VAR_DEC_IDENTIFIER = "^(" + TYPE + ")";
	public static final String VAR_DEC_PATTERN =
			TYPE + VAR_NAME + "(" + "=" + ASSIGNABLE_VALUE + ")?" + VAR_DEC_LIST + END_COMMAND;

	public static final String VAR_ASSIGNMENT_IDENTIFIER = "^(" + VAR_NAME + "=" + ")";
	public static final String VAR_ASSIGNMENT_PATTERN = VAR_NAME + "=" + ASSIGNABLE_VALUE +
														END_COMMAND;

	public static final String FINAL_VAR_DEC_IDENTIFIER = "^(" + FINAL + ")";
	public static final String FINAL_VAR_DEC_PATTERN =
			FINAL + TYPE + VAR_NAME + "=" + ASSIGNABLE_VALUE + "(" + "," + VAR_NAME + "=" +
			ASSIGNABLE_VALUE +
			")*" + END_COMMAND;

	public static final String FUNC_DEC_IDENTIFIER = "^(" + VOID + ")";
	public static final String FUNC_DEC_PATTERN = VOID + FUNC_NAME + LEFT_BRACKET +
												  PARAMETER_DEC_LIST +
												  RIGHT_BRACKET + LEFT_CURLY_BRACKET;

	public static final String FUNC_CALL_IDENTIFIER =
			"^(" + FUNC_NAME + LEFT_BRACKET + ")";
	public static final String FUNC_CALL_PATTERN =
			FUNC_NAME + LEFT_BRACKET + ARGUMENTS_LIST + RIGHT_BRACKET + END_COMMAND;

	public static final String IF_IDENTIFIER = "^(" + IF + ")";
	public static final String IF_PATTERN =
			IF + LEFT_BRACKET + CONDITION_LIST + RIGHT_BRACKET + LEFT_CURLY_BRACKET;

	public static final String WHILE_IDENTIFIER = "^(" + WHILE + ")";
	public static final String WHILE_PATTERN =
			WHILE + LEFT_BRACKET + CONDITION_LIST + RIGHT_BRACKET + LEFT_CURLY_BRACKET;

	public static final String END_SCOPE_IDENTIFIER = "^(" + RIGHT_CURLY_BRACKET + ")";
	public static final String END_SCOPE_PATTERN = RIGHT_CURLY_BRACKET;

	public static final String COMMENT_IDENTIFIER = "^(" + COMMENT_START + ")";
	public static final String COMMENT_PATTERN = COMMENT_START + ".*";

	public static final String RETURN_IDENTIFIER = "^(" + RETURN + ")";
	public static final String RETURN_PATTERN = RETURN + END_COMMAND;

	public static final String EMPTY_LINE_IDENTIFIER = "^" + OPTIONAL_SPACES + "$";
	public static final String EMPTY_LINE_PATTERN = "^" + OPTIONAL_SPACES + "$";


	// regexes for the LineToSymbolTable
	public static final String FIND_TYPES_REGEX = "(" + OR_TYPES + ")";
	public static final String FIND_VAR_NAME_REGEX = VOID + "(" + VAR_NAME_NO_SPACES + ")" + "\\W";


	/**
	 * @return a regex that represents the "or" operation on all the types. (without grouping them)
	 */
	private static String getOrTypeRegex() {
		StringBuilder orTypeRegex = new StringBuilder(); // begin to build the regex
		boolean firstValue = true;

		for (Type type : Type.values()) {  // adding the different types to the regex
			if (!firstValue) {
				orTypeRegex.append("|");
			}
			orTypeRegex.append(type.str());
			firstValue = false;
		}
		return orTypeRegex.toString(); // returns the regex
	}

	/**
	 * this method returns a regex that represents the "or" operation on the given types. (without
	 * grouping them)
	 * @param typeArray an array of Type objects.
	 * @return a regex that represents the "or" operation on the given types. (without grouping
	 * 		them).
	 */
	private static String getOrTypeRegex(Type[] typeArray) {
		StringBuilder orTypeRegex = new StringBuilder(); // begin to build the regex
		boolean firstValue = true;

		for (Type type : typeArray) {  // adding the different types to the regex
			if (!firstValue) {
				orTypeRegex.append("|");
			}
			orTypeRegex.append(type.str());
			firstValue = false;
		}
		return orTypeRegex.toString(); // returns the regex
	}

	/**
	 * @return a regex that represents the "or" operation on all the keywords. (without grouping
	 * 		them)
	 */
	private static String getOrKeywordRegex() {
		StringBuilder orTypeRegex = new StringBuilder(); // begin to build the regex
		boolean firstValue = true;

		for (KeyWord keyWord : KeyWord.values()) {  // adding the different types to the regex
			if (!firstValue) {
				orTypeRegex.append("|");
			}
			orTypeRegex.append(keyWord.string());
			firstValue = false;
		}
		return orTypeRegex.toString(); // returns the regex
	}

	/**
	 * this method returns a regex that represents the "or" operation on the given keywords.
	 * (without grouping them)
	 * @param keywordArray an array of KeyWord objects.
	 * @return a regex that represents the "or" operation on the given keywords. (without grouping
	 * 		them)
	 */
	private static String getOrKeywordRegex(KeyWord[] keywordArray) {
		StringBuilder orTypeRegex = new StringBuilder(); // begin to build the regex
		boolean firstValue = true;

		for (KeyWord keyWord : keywordArray) {  // adding the different types to the regex
			if (!firstValue) {
				orTypeRegex.append("|");
			}
			orTypeRegex.append(keyWord.string());
			firstValue = false;
		}
		return orTypeRegex.toString(); // returns the regex
	}

}
