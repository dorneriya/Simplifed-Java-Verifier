package sjavac.symbolTables;

import sjavac.enums.Type;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class represent variables tables of a program
 */
public class VarTables {

	/* linked list of variables tables. in FIFO perspective. */
	private final LinkedList<ScopeTable> tables;
	private ScopeTable globalsTable;

	/**
	 * var Tables constructor
	 */
	public VarTables() {
		this.tables = new LinkedList<>();
		this.globalsTable = new ScopeTable();
	}

	/**
	 * This method determines the status of the global variables of the program.
	 */
	public void fixGlobalTable() {
		this.globalsTable = tables.get(0);
	}

	/**
	 * This method starts a new method's scope, so that the information about the global variables
	 * is not affected by other methods that ran before.
	 */
	public void startNewMethodScope() {
		ScopeTable global = new ScopeTable();
		global.copyScopeTable(globalsTable);

		tables.pop();
		tables.push(global);
		tables.push(new ScopeTable());
	}


	/**
	 * call every time we start new scope
	 */
	public void newScope() {
		tables.push(new ScopeTable());

	}

	/**
	 * call every time we got to the end of some scope
	 */
	public void endScope() {
		tables.pop();
	}

	/**
	 * This method used in order to detect an invalid decleration of some variable.
	 * @param name a variable name
	 * @return True if this name has already been declared, false ow
	 */
	public boolean containInScope(String name) {
		return tables.get(0).variables.containsKey(name);
	}

	/**
	 * This method used in order to find a variable in the nearest scope.
	 * @param name a variable name
	 * @return True if this name has already been declared, false ow.
	 */
	public boolean contain(String name) {
		for (ScopeTable table : tables) {
			if (table.contain(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param name variable name
	 * @param type variable type
	 * @param initialize is the variable initialize
	 * @param isFinal boolean value of weather this variable is final or not.
	 * @throws MultipleSameVarNameException thrown in case the variable we try to declare is
	 * 		already declared in this scope.
	 */
	public void define(String name, Type type, boolean initialize, boolean isFinal)
			throws MultipleSameVarNameException {
		if (this.containInScope(name)) {
			throw new MultipleSameVarNameException(name);
		}
		tables.get(0).define(name, type, initialize, isFinal);
	}


	/**
	 * @param name a variable name
	 */
	public void setInitialize(String name) {
		for (ScopeTable table : tables) {
			if (table.contain(name)) {
				table.setInitialize(name);
				return;
			}
		}
	}

	/**
	 * This method returns the type of variable, when the search is performed starting from the
	 * last
	 * defined scope and up.
	 * @param name variable name
	 * @return the variable type
	 */
	public Type typeOf(String name) {
		for (ScopeTable table : tables) {
			if (table.contain(name)) {
				return table.typeOf(name);
			}
		}
		return null;
	}

	/**
	 * This method returns true if the variable initialize, when the search is performed starting
	 * from the last defined scope and up.
	 * @param name variable name
	 * @return true if the variable initialize, false ow
	 */
	public boolean isInitialize(String name) {
		for (ScopeTable table : tables) {
			if (table.contain(name)) {
				return table.isInitialize(name);
			}
		}
		return false;
	}

	/**
	 * @param name a variable name
	 * @return true if this variable is final, false ow
	 */
	public boolean isFinal(String name) {
		for (ScopeTable table : tables) {
			if (table.contain(name)) {
				return table.isFinal(name);
			}
		}
		return false;
	}

	/**
	 * set all the variables in the current scope as initialized, used to handle with method's
	 * arguments
	 */
	public void setArgsInitialize() {
		for (String name : tables.get(0).variables.keySet()) {
			setInitialize(name);
		}
	}

	/**
	 * @param name a variable name
	 */
	public void setFinal(String name) {
		for (ScopeTable table : tables) {
			if (table.contain(name)) {
				table.setFinal(name);
				return;
			}
		}
	}

	/**
	 * This class represent a table of single scope variables
	 */
	private static class ScopeTable {
		private final HashMap<String, Variable> variables;

		/**
		 * ScopeTable constructor
		 */
		public ScopeTable() {
			variables = new HashMap<>();
		}

		/**
		 * define new variable in this scopeTable
		 * @param name the name of the variable
		 * @param type the type of the variable
		 * @param initialize if the variable initialize
		 * @param isFinal if the variable final
		 */
		private void define(String name, Type type, boolean initialize, boolean isFinal) {
			variables.put(name, new Variable(name, type, initialize, isFinal));
		}

		/**
		 * @param name a variable name
		 * @return true if the scopeTable contain this variable, false ow
		 */
		private boolean contain(String name) {
			return variables.containsKey(name);
		}

		/**
		 * @param name a variable name
		 * @return the variable type
		 */
		private Type typeOf(String name) {
			return variables.get(name).type;
		}

		/**
		 * @param name a variable name
		 * @return true if this variable initialize, false ow
		 */
		private boolean isInitialize(String name) {
			return variables.get(name).isInitialize;
		}

		/**
		 * @param name a variable name
		 */
		private void setInitialize(String name) {
			variables.get(name).isInitialize = true;
		}

		/**
		 * @param name a variable name
		 * @return true if this variable final, false ow
		 */
		private boolean isFinal(String name) {
			return variables.get(name).isFinal;
		}

		/**
		 * @param name a variable name
		 */
		public void setFinal(String name) {
			variables.get(name).isFinal = true;
		}

		/**
		 * This method copies the values of one scope of var tables to another tabble, and sets it
		 * as the current scope.
		 * @param globalsTable the scope table to copy from.
		 */
		public void copyScopeTable(ScopeTable globalsTable) {
			for (String var : globalsTable.variables.keySet()) {
				Variable copy = new Variable(globalsTable.variables.get(var));
				this.variables.put(var, copy);
			}
		}
	}

	/**
	 * This class represent a variable
	 */
	private static class Variable implements Cloneable {
		String name;
		Type type;
		boolean isInitialize;
		boolean isFinal;

		/**
		 * @param name the name of the variable
		 * @param type the type of the variable
		 * @param isInitialize if the variable initialize
		 * @param isFinale if the variable final
		 */
		Variable(String name, Type type, boolean isInitialize, boolean isFinale) {
			this.name = name;
			this.type = type;
			this.isInitialize = isInitialize;
			this.isFinal = isFinale;
		}

		/**
		 * copy constructor
		 * @param toCopy variable to copy
		 */
		public Variable(Variable toCopy) {
			this.name = toCopy.name;
			this.type = toCopy.type;
			this.isInitialize = toCopy.isInitialize;
			this.isFinal = toCopy.isFinal;
		}
	}


}
