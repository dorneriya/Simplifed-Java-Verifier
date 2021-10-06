package sjavac.enums;
import sjavac.utils.regexConsts;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * an ENUM of the possible types in sjava.
 */
public enum Type {
	INT("int", regexConsts.INT_ASSIGN),
	STRING("String", regexConsts.STRING_ASSIGN),
	CHAR("char", regexConsts.CHAR_ASSIGN),
	DOUBLE("double", regexConsts.DOUBLE_ASSIGN),
	BOOLEAN("boolean", regexConsts.BOOLEAN_ASSIGN);

	private final String stringRepresentation;
	private final String regex;
	private final Pattern regexPattern;


	/**
	 * a constructor to build a Type object
	 * @param stringRepresentation the string representation of the type.
	 * @param regex the regex of this type (string representation).
	 */
	Type(String stringRepresentation, String regex) {
		this.stringRepresentation = stringRepresentation;
		this.regex = regex;
		this.regexPattern = Pattern.compile(regex);
	}

	/**
	 * this method checks if a certain string representing a value, up to spaces before and after
	 * @param string the string of the represented value
	 * @return true iff the string represents a value of this certain Type.
	 */
	public boolean isType(String string) {
		Matcher m = this.regexPattern.matcher(string);
		return m.matches();
	}


	/**
	 * this method returns the type of a string up to spaces before and after. e.g "typeOf("10
	 * .56")
	 * = double.
	 * @param string a string of some value.
	 * @return the Type of this value, if exists, null otherwize
	 */
	public static Type typeOf(String string) {
		for (Type type : Type.values()) {
			if (type.isType(string)) {
				return type;
			}
		}
		return null;
	}

	/**
	 * this method returns an arrayList of Types that are valid inputs to a variable of type t.
	 * @param t var type to assign to.
	 * @return an arrayList of Types that are valid inputs to a variable of type t.
	 */
	public static ArrayList<Type> validInputs(Type t) {
		ArrayList<Type> validTypes = new ArrayList<>();
		switch (t) {
		case BOOLEAN:
			validTypes.add(BOOLEAN);
			validTypes.add(INT);
			validTypes.add(DOUBLE);
			break;
		case STRING:
			validTypes.add(STRING);
			break;
		case DOUBLE:
			validTypes.add(INT);
			validTypes.add(DOUBLE);
			break;
		case CHAR:
			validTypes.add(CHAR);
			break;
		case INT:
			validTypes.add(INT);
			break;
		}
		return validTypes;
	}

	/**
	 * @return the Type's String representation.
	 */
	public String str() {
		return this.stringRepresentation;
	}
}


