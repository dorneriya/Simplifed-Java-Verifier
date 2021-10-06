package oop.ex6.symbolTables;

import oop.ex6.enums.Type;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represent a table of function of the current program
 */
public class FuncTable {

	private final HashMap<String, Method> functions;

	/**
	 * create new FuncTable
	 */
	public FuncTable() {
		functions = new HashMap<>();
	}

	/**
	 * Define a new function
	 * @param name the function name
	 * @param parameters the function parameters
	 * @throws MultipleSameFuncNamesException if there is already a function with this name
	 */
	public void define(String name, ArrayList<Type> parameters)
			throws MultipleSameFuncNamesException {
		if (this.contains(name)) {
			throw new MultipleSameFuncNamesException(name);
		}
		functions.put(name, new Method(name, parameters));
	}

	/**
	 * @param name a method name
	 * @return true if the table contain this method, false ow
	 */
	public boolean contains(String name) {
		return functions.containsKey(name);
	}

	/**
	 * @param name the function name
	 * @param args the argument sent to the function
	 * @return true if the arguments compatible with the function parameters, false ow
	 */
	public boolean compatible(String name, ArrayList<Type> args) {
		ArrayList<Type> funcParamType = functions.get(name).parameters;
		if (args.size() != funcParamType.size()) {
			return false;
		}
		for (int i = 0; i < args.size(); i++) {
			if (!Type.validInputs(funcParamType.get(i)).contains(args.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * this class represent a method in a program
	 */
	private static class Method {
		String name;
		ArrayList<Type> parameters;

		/**
		 * @param name the method name
		 * @param parameters the method parameters
		 */
		private Method(String name, ArrayList<Type> parameters) {
			this.name = name;
			this.parameters = parameters;
		}
	}
}
