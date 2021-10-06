package oop.ex6.utils;

import oop.ex6.secondPass.FileLogicException;

/**
 * thrown in case a final variable was declared but not assigned.
 */
public class UnInitializedFinalException extends FileLogicException {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor.
	 */
	public UnInitializedFinalException() {
		this.errorMsg = "tried to declare a final variable without initializing it.";
	}
}
