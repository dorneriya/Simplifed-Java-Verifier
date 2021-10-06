package oop.ex6.secondPass;

import oop.ex6.enums.*;
import oop.ex6.firstpass.classifylines.LineKind;
import oop.ex6.symbolTables.*;
import oop.ex6.utils.*;

import java.io.*;
import java.util.ArrayList;


/**
 * this class does the second pass on the sjava file - checks for logic problems in the file.
 */
public class SecondPasser {
	private final BufferedReader file; // the code file
	private int lineNum;
	private final LineKind[] kindsList;
	private final FuncTable funcTable;
	private final VarTables varTable;
	private static final String EMPTY_STRING = "";
	private static final int OFFSET = 1;
	private LineKind previousLineKind; // this variable used for check that each method ends with
	// return.

	/**
	 * @param funcTable the program functions table
	 * @param varTable the program variables table
	 * @param path the program file path
	 * @param kindsList the line kind list.
	 * @throws FileNotFoundException If we can't file the find.
	 */
	public SecondPasser(FuncTable funcTable, VarTables varTable, String path, LineKind[] kindsList)
			throws FileNotFoundException {
		this.file = new BufferedReader(new FileReader(path));
		this.lineNum = 0;
		this.kindsList = kindsList;
		this.funcTable = funcTable;
		this.varTable = varTable;
	}

	/**
	 * main method of this module - runs the second pass and check file logic problems.
	 * @throws IOException If IOException occurred.
	 * @throws FileLogicException If there is a logic Error in the file.
	 */
	public void pass()
			throws IOException, FileLogicException {
		String line = this.file.readLine();
		try {
			while (line != null) {
				if (lineNum == kindsList.length) {
					return;
				}
				LineKind kind = this.kindsList[lineNum];
				this.lineNum += OFFSET;
				if (kind != LineKind.FUNC_DEC) {
					line = this.file.readLine();
					continue;
				} else {
					compileMethod(line);
				}
				line = this.file.readLine();
			}
			this.file.close();
		} catch (FileLogicException e) {
			throw new FileLogicException(e, this.lineNum);
		}

	}

	/**
	 * @param line the current line in the file;
	 * @throws IOException If IOException occurred.
	 * @throws FileLogicException If there is a logic Error in the file.
	 */
	private void compileMethod(String line)
			throws IOException, FileLogicException {
		compileMethodDec(line);
		compileStatements();
		if (previousLineKind != LineKind.RETURN) {
			throw new NoReturnEndOfFunctionException();
		}
		varTable.endScope();
	}

	/**
	 * this method gets a method declaration line and updates the variablesTable with it's values.
	 * @param line the line of the method declaration
	 * @throws FileLogicException thrown in case one of the arguments is invalid (i.e defined in
	 * 		this scope twice)
	 */
	private void compileMethodDec(String line) throws FileLogicException {
		varTable.startNewMethodScope();
		String[] arguments = getArguments(line);
		for (String arg : arguments) {
			if (arg.equals(EMPTY_STRING)) {
				break;
			}
			if (arg.contains(KeyWord.FINAL.string())) {
				try {
					LineToSymbolTable.lineToVarTables(arg, varTable, LineKind.FINAL_VAR_DEC);
				} catch (UnInitializedFinalException ignored) {
				}
			} else {
				LineToSymbolTable.lineToVarTables(arg, varTable, LineKind.VAR_DEC);
			}
		}
		varTable.setArgsInitialize();
	}


	/**
	 * This method gets a line of function declaration, and returns an array of strings, each is an
	 * argument.
	 * @param line String of a function declaration
	 * @return an array of strings, each is an argument.
	 */
	private String[] getArguments(String line) {
		int[] brackets = bracketsIndex(line);
		String arguments_line = line.substring(brackets[0], brackets[1]);
		arguments_line = arguments_line.trim();
		return arguments_line.split(regexConsts.COMMA);
	}


	/**
	 * @throws IOException If IOException occurred.
	 * @throws FileLogicException If there is a logic Error in the file.
	 */
	private void compileStatements()
			throws IOException, FileLogicException {
		String line = this.file.readLine();
		while (line != null) {
			LineKind kind = this.kindsList[lineNum];
			this.lineNum += OFFSET;
			switch (kind) {
			case VAR_DEC:
			case VAR_ASSIGNMENT:
			case FINAL_VAR_DEC:
				LineToSymbolTable.lineToVarTables(line, varTable, kind);
				break;
			case WHILE:
			case IF:
				compileIfWhile(line);
				break;
			case RETURN:
				break;
			case END_SCOPE:
				return;
			case FUNC_CALL:
				compileFuncCall(line);
			default: // empty_line, comment
				break;
			}
			if (kind != LineKind.COMMENT && kind != LineKind.EMPTY_LINE) {
				previousLineKind = kind;
			}
			line = this.file.readLine();
		}
		this.lineNum += OFFSET;
	}

	/**
	 * @param line the current line in the file;
	 * @throws FileLogicException If there is a logic Error in the file.
	 */
	private void compileFuncCall(String line) throws FileLogicException {
		int[] brackets = bracketsIndex(line);
		String funcName = line.substring(0, brackets[0] - OFFSET);
		funcName = funcName.trim();
		checkResolvedFunction(funcName);
		ArrayList<Type> argsType = getArgsTypes(line.substring(brackets[0], brackets[1]));
		if (!funcTable.compatible(funcName, argsType)) {
			throw new InvalidArgException();
		}
	}

	/**
	 * this method gets a string of arguments given to a function and returns an arrayList of their
	 * Types
	 * @param argsString a String of arguments.
	 * @return an arrayList of Types.
	 * @throws InvalidArgException thrown in case one of the arguments is not valid.
	 */
	private ArrayList<Type> getArgsTypes(String argsString) throws InvalidArgException {
		ArrayList<Type> argsType = new ArrayList<>();
		String[] funcArgs = argsString.split(",");
		for (String arg : funcArgs) {
			arg = arg.trim();
			if (arg.equals(EMPTY_STRING)) {
				break;
			}
			insertArgToTypeList(argsType, arg);
		}
		return argsType;
	}

	/**
	 * This method gets a string of an argument given to a function and a Types list and adds it to
	 * the list if it's valid.
	 * @param argsTypeList the list of types to add to
	 * @param arg a string of the argument
	 * @throws InvalidArgException thrown in case the argument is invalid.
	 */
	private void insertArgToTypeList(ArrayList<Type> argsTypeList, String arg)
			throws InvalidArgException {
		Type type = Type.typeOf(arg); // get the args type
		if (type == null) {
			if (!varTable.contain(arg) || !varTable.isInitialize(arg)) { // check it
				throw new InvalidArgException(arg);
			} else {
				argsTypeList.add(varTable.typeOf(arg));
			}
		} else {
			argsTypeList.add(type);
		}
	}

	/**
	 * This method checks a function is resolved (defined in the FuncTable)
	 * @param funcName the function name
	 * @throws UnresolvedMethodException thrown if function is not resolved
	 */
	private void checkResolvedFunction(String funcName) throws UnresolvedMethodException {
		if (!funcTable.contains(funcName)) {
			throw new UnresolvedMethodException(funcName);
		}
	}

	/**
	 * @param line the current line in the file.
	 * @throws FileLogicException If there is a logic Error in the file.
	 * @throws IOException If IOException occurred.
	 */
	private void compileIfWhile(String line)
			throws IOException, FileLogicException {
		varTable.newScope();
		compileCondition(line);
		compileStatements();
		varTable.endScope();
	}


	/**
	 * @param line the current line in the file.
	 * @throws InvalidConditionException if there is an Error with thw condition
	 */
	private void compileCondition(String line) throws InvalidConditionException {
		int[] brackets = bracketsIndex(line);
		String condition = line.substring(brackets[0], brackets[1]);
		condition = condition.replaceAll(regexConsts.REGULAR_SPACE, EMPTY_STRING);
		String[] conditionArgs = condition.split(regexConsts.LOGIC_OPERATOR);
		for (String arg : conditionArgs) {
			checkConditionValidity(arg);
		}
	}

	/**
	 * This method gets a string of condition and checks if it is valid. if not throws an exception
	 * @param arg String of the argument given as condition
	 * @throws InvalidConditionException thrown in case the condition is not valid.
	 */
	private void checkConditionValidity(String arg) throws InvalidConditionException {
		Type type = Type.typeOf(arg);
		if (type == null) {
			if (!varTable.contain(arg) || !varTable.isInitialize(arg) ||
				!Type.validInputs(Type.BOOLEAN).contains(varTable.typeOf(arg))) {
				throw new InvalidConditionException(arg);
			}
		} else if (!Type.validInputs(Type.BOOLEAN).contains(type)) {
			throw new InvalidConditionException(arg);
		}
	}

	/**
	 * returns the indexes of the brackets in the line.
	 * @param line line to parse.
	 * @return an array of two indexes, one of the left bracket and one of the right bracket.
	 */
	private int[] bracketsIndex(String line) {
		return new int[]{line.indexOf(regexConsts.R_BRACKET) + OFFSET,
				line.indexOf(regexConsts.L_BRACKET)};
	}
}
