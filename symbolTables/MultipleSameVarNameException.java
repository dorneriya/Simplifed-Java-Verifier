package oop.ex6.symbolTables;

import oop.ex6.secondPass.FileLogicException;

/**
 * thrown in case a variable we try to declare has already been declared.
 */
public class MultipleSameVarNameException extends FileLogicException {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * @param name the variable name.
	 */
	MultipleSameVarNameException(String name) {
		this.errorMsg = String.format("the variable '%s' is already declared", name);
	}
}
