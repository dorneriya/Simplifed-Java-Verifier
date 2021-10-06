package oop.ex6.secondPass;

/**
 * thrown in case an argument is invalid
 */
public class InvalidArgException extends FileLogicException {
	private static final long serialVersionUID = 1L;
	/**
	 * constructor
	 * @param name the name of the invalid variable.
	 */
	InvalidArgException(String name){
		this.errorMsg = String.format("argument '%s' is Invalid", name);
	}

	/**
	 * default constructor.
	 */
	InvalidArgException(){
		this.errorMsg = "An invalid argument was given to function";
	}
}
