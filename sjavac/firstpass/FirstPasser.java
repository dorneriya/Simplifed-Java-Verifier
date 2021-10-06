package sjavac.firstpass;

import sjavac.secondPass.FileLogicException;
import sjavac.utils.LineToSymbolTable;
import sjavac.firstpass.classifylines.*;
import sjavac.symbolTables.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * this class does the first pass on a file to compile - updates symbol tables of global variables
 * and functions, and checks for validity in the structure if the code.
 */
public class FirstPasser {

	private final BufferedReader file; // the code file
	private int lineNum;
	private final LinkedList<LineKind> scope;   // a linked list of the scopes we are in
	private final Identifier identifier; // identifies lines to their kind
	private final PatternCheck patternCheck;  // checks a line is compatible to it's presumed type.
	private final ArrayList<LineKind> kindsList;
	private final FuncTable funcTable;
	private final VarTables varTables;

	/**
	 * @param path path to the file to read from
	 * @param identifier an object implementing Identifier interface.
	 * @param patternChecker an object implementing PatternCheck interface.
	 * @param funcTable a function symbolTable.
	 * @param varTables a variables symbolTable.
	 * @throws IOException thrown if file not found or problem opening the file
	 */
	public FirstPasser(String path, Identifier identifier, PatternCheck patternChecker,
					   FuncTable funcTable,
					   VarTables varTables) throws IOException {
		this.file = new BufferedReader(new FileReader(path));
		this.lineNum = 0;
		this.identifier = identifier;
		this.patternCheck = patternChecker;
		this.scope = new LinkedList<>();
		this.kindsList = new ArrayList<>();
		this.funcTable = funcTable;
		this.varTables = varTables;
	}

	/**
	 * this method does the first pass of the compiler - initializes global variables and checks
	 * for
	 * structural validity of the code.
	 * @return true iff the file is compilable.
	 * @throws IOException thrown if there is a problem reading from the file
	 * @throws NoIdendtiferMatch thrown if a line's kind can't be identified.
	 * @throws FileLogicException thrown in case there has been a problem with file logic (not
	 * 		structure).
	 */
	public boolean firstPass() throws IOException, FileStructureException, FileLogicException {
		String line = this.file.readLine();
		varTables.newScope(); // the first scope and the only one we fill in the first pass
		try {
			while (line != null) {
				this.lineNum += 1;
				LineKind kind = this.identifier.identify(line);
				if (!this.patternCheck.checkPattern(line, kind)) {
					throw new PatternException(kind);
				}
				this.checkScope(kind);
				this.updateSymbolTable(line, kind);
				this.updateScope(kind); // update the scopes LinkedList
				this.kindsList.add(kind);  // update the kinds array.
				line = this.file.readLine();
			}
			checkScopesClosed();
			this.varTables.fixGlobalTable();
			this.file.close();
			return true;
		} catch (FileStructureException e) {
			throw new FileStructureException(e, line, this.lineNum);
		} catch (FileLogicException e) {
			throw new FileLogicException(e, line, this.lineNum);
		}
	}

	/**
	 * This method checks if a certain line kind can be in the current scope
	 * @param kind a LineKind
	 * @throws InsideFuncException thrown in case a statement thet cannot be inside a function is
	 * 		inside a function.
	 * @throws OutsideFuncException thrown in case a statement that cannot be outnside a function
	 * 		is outside a function.
	 */
	private void checkScope(LineKind kind) throws OutsideFuncException, InsideFuncException {
		if (kind == LineKind.RETURN || kind == LineKind.END_SCOPE || kind == LineKind.IF ||
			kind == LineKind.WHILE || kind == LineKind.FUNC_CALL) {
			if (!scope.contains(LineKind.FUNC_DEC)) {
				throw new OutsideFuncException(kind);
			}
		} else if (kind == LineKind.FUNC_DEC) {
			if (scope.contains(LineKind.FUNC_DEC)) {
				throw new InsideFuncException(kind);
			}
		}
	}

	/**
	 * checks if a scope needed to be closed and did not.
	 * @throws ScopeNotClosedException thrown in case a scope needed to be closed and did not.
	 */
	private void checkScopesClosed() throws ScopeNotClosedException {
		if (this.scope.size() > 0) {
			throw new ScopeNotClosedException();
		}
	}

	/**
	 * updates the scope if needed (e.g in the entering to if statement).
	 * @param kind the LineKind of the current line.
	 */
	private void updateScope(LineKind kind) {
		if (kind == LineKind.END_SCOPE) {
			this.scope.pop();
		} else if (kind == LineKind.FUNC_DEC || kind == LineKind.WHILE || kind == LineKind.IF) {
			this.scope.push(kind);
		}
	}

	/**
	 * this method returns an array of the line kinds of the s-java file, in the order they appear.
	 * @return an array of the line kinds of the s-java file, in the order they appear.
	 */
	public LineKind[] lineKindsArray() {
		LineKind[] lineKindsArray = new LineKind[this.kindsList.size()];
		// move the LineKinds to array:
		for (int i = 0; i < this.kindsList.size(); i++) {
			lineKindsArray[i] = this.kindsList.get(i);
		}
		return lineKindsArray;
	}

	/**
	 * @param line the line from which we want to update the line symbol table.
	 * @param lineKind the line's kind.
	 * @throws FileLogicException thrown in case one of the variables or function name has already
	 * 		been declared in the current scope.
	 */
	private void updateSymbolTable(String line, LineKind lineKind)
			throws FileLogicException {
		if (this.scope.size() > 0) { // global variable only.
			return;
		}
		switch (lineKind) {
		case VAR_ASSIGNMENT:
		case VAR_DEC:
		case FINAL_VAR_DEC:
			LineToSymbolTable.lineToVarTables(line, varTables, lineKind);
			return;
		case FUNC_DEC:
			LineToSymbolTable.lineToFuncSymbolTable(line, this.funcTable);
		}
	}

}
