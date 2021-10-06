package sjavac.utils;

import sjavac.secondPass.FileLogicException;
import sjavac.enums.Type;
import sjavac.firstpass.classifylines.LineKind;
import sjavac.symbolTables.*;
import sjavac.secondPass.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;

/**
 * This is a class of static methods that gets a line of a variable or function and put it into the
 * relevant table. Within the process the methods check the validity of the line logically.
 */
public class LineToSymbolTable {

	private static final Pattern findTypesPattern = Pattern.compile(regexConsts.FIND_TYPES_REGEX);
	private static final Pattern findFuncNamePattern = Pattern
			.compile(regexConsts.FIND_VAR_NAME_REGEX);
	// a hashmap from all the Types String representations to their Type objects.
	private static final HashMap<String, Type> stringToKind = new HashMap<String, Type>() {{
		for (Type type : Type.values()) {
			put(type.str(), type);
		}
	}};

	/**
	 * This method defines a new function based on a function declaration line.
	 * @param line line of function declaration
	 * @param funcTable a functions symbol table
	 * @throws MultipleSameFuncNamesException thrown in case a function we rty to declare is
	 * 		already declared.
	 */
	public static void lineToFuncSymbolTable(String line, FuncTable funcTable)
			throws MultipleSameFuncNamesException {
		ArrayList<Type> types = new ArrayList<>(); // Types this function gets as parameters.
		Matcher typesMatcher = findTypesPattern.matcher(line); // matches each type
		Matcher funcNameMatcher = findFuncNamePattern.matcher(line); // matches each varName.
		funcNameMatcher.find();
		String funcName = funcNameMatcher.group(1); // get the function name
		while (typesMatcher.find()) {
			types.add(stringToKind.get(typesMatcher.group(1)));  // add types to types arraylist
		}
		funcTable.define(funcName, types);
	}

	/**
	 * @param line A line containing a reference to a variable
	 * @param varTable a varTable object
	 * @param kind the line kind
	 * @throws FileLogicException If there is a logic Error in the line.
	 */
	public static void lineToVarTables(String line, VarTables varTable, LineKind kind)
			throws FileLogicException {
		line = line.trim();
		Matcher typesMatcher = findTypesPattern.matcher(line);
		switch (kind) {
		case VAR_DEC:
			compileVarDec(line, typesMatcher, varTable);
			break;
		case VAR_ASSIGNMENT:
			compileVarAssignment(line, varTable);
			break;
		case FINAL_VAR_DEC:
			String[] varNames = compileVarDec(line, typesMatcher, varTable);
			for (String name : varNames) {
				varTable.setFinal(name);
				if (!varTable.isInitialize(name)) {
					throw new UnInitializedFinalException();
				}
			}
		}
	}

	/**
	 * gets a line declaring a variable and if it is correct updates the table.
	 * @param line A line containing a variable deceleration
	 * @param typesMatcher a matcher object. catch a "type" string.
	 * @param table a varTable object
	 * @return a string array with the names of all the variables declared.
	 * @throws InvalidAssignException if the value we try to assign isn't valid.
	 * @throws MultipleSameVarNameException thrown in case a variable we try to declare is already
	 * 		declared in this scope.
	 */
	private static String[] compileVarDec(String line, Matcher typesMatcher, VarTables table)
			throws InvalidAssignException, MultipleSameVarNameException {
		typesMatcher.find();
		Type decType = stringToKind.get(typesMatcher.group(1));
		String lineVars = line.substring(typesMatcher.end());
		String[] vars = lineVars.split(regexConsts.COMMA);
		String[] names = new String[vars.length];
		int i = 0;
		for (String var : vars) { // for all the variables in the declaration
			var = var.replaceAll(regexConsts.LEFTOVERS, regexConsts.EMPTY_STRING);
			String[] assign = var.split(regexConsts.EQUAL);
			String name = assign[0];
			names[i] = name;
			i++;
			if (assign.length == 2) { // there is an assignment
				String val = assign[1];
				assignmentToSymbolTable(table, decType, name, val);
			} else if (assign.length == 1) { //just a deceleration
				table.define(name, decType, false, false);
			}

		}
		return names;
	}

	/**
	 * This method takes an assignment, checks it's validity and updates the given symbolTable
	 * @param table table to update.
	 * @param decType the type of the variable to add to the table.
	 * @param varName String of the variable name
	 * @param val String the value to assign to it
	 * @throws InvalidAssignException thrown in case the value does not match the variable type.
	 * @throws MultipleSameVarNameException thrown in case the variable is already defined in this
	 * 		scope.
	 */
	private static void assignmentToSymbolTable(VarTables table, Type decType, String varName,
												String val)
			throws InvalidAssignException, MultipleSameVarNameException {
		if (table.contain(val)) {
			Type valType = table.typeOf(val);
			if (!Type.validInputs(decType).contains(valType) || !table.isInitialize(val)) {
				throw new InvalidAssignException(varName, val);
			}
		} else if (!valFitType(decType, val)) {
			throw new InvalidAssignException(varName, val);
		}
		table.define(varName, decType, true, false);
	}


	/**
	 * gets a line in which a value is assigned to the variable and if it is valid, updates the
	 * table
	 * @param line A line containing a reference to a variable
	 * @param table a varTable object
	 * @throws InvalidAssignException if the value we try to assign isn't valid.
	 */
	private static void compileVarAssignment(String line, VarTables table)
			throws InvalidAssignException {
		line = line.replaceAll(regexConsts.LEFTOVERS, regexConsts.EMPTY_STRING);
		String[] lineParts = line.split(regexConsts.EQUAL);
		String varName = lineParts[0];
		String val = lineParts[1];
		// check the variable
		if (!table.contain(varName) || table.isFinal(varName)) {
			throw new InvalidAssignException(varName, val);
		}
		Type varType = table.typeOf(varName);
		// check the value
		if (table.contain(val)) {
			Type valType = table.typeOf(val);
			if (!Type.validInputs(varType).contains(valType) || !table.isInitialize(val)) {
				throw new InvalidAssignException(varName, val);
			}
		} else if (!valFitType(varType, val)) {
			throw new InvalidAssignException(varName, val);
		}
		table.setInitialize(varName);
	}

	/**
	 * checks that the value given corresponds to the variable type
	 * @param type the variable type
	 * @param value the value given
	 * @return true if the value fit to the type, false ow
	 */
	private static boolean valFitType(Type type, String value) {
		return Type.validInputs(type).contains(Type.typeOf(value));
	}
}
