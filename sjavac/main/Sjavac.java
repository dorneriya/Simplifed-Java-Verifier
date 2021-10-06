package sjavac.main;

import sjavac.firstpass.*;
import sjavac.firstpass.classifylines.*;
import sjavac.secondPass.SecondPasser;
import sjavac.symbolTables.*;

import java.io.*;

/**
 * the sjavac.main class that runs the sjava compiler.
 */
public class Sjavac {

	public static int SUCCESS = 0;
	public static int S_JAVA_EXCEPTION = 1;
	public static int IO_EXCEPTION = 2;

	private static final String USAGE_ERROR_MSG = "USAGE: arguments error\n";
	private static final String IOEXCEPTION_ERROR_MSG = "IOException occurred\n";


	/**
	 * the sjavac.main method - gets a file path and prints out the result of weather it was "compiled" correctly
	 * or not.
	 * @param args path to sjava file.
	 */
	public static void main(String[] args) {
		if (args.length != 1){
			System.err.println(USAGE_ERROR_MSG);
			return;
		}
		FuncTable funcTable = new FuncTable();
		VarTables varTable = new VarTables();
		LineKind[] lineKindList;
		try{
			FirstPasser firstPass = new FirstPasser(args[0], new IdentifyLine(), new PatternChecker(),
													funcTable, varTable);
			firstPass.firstPass();
			lineKindList = firstPass.lineKindsArray();
			SecondPasser pass2 = new SecondPasser(funcTable,varTable,args[0],lineKindList);
			pass2.pass();
			System.out.println(SUCCESS);
		} catch (Ex6Exception error) {
			error.printMsg();
			System.out.println(S_JAVA_EXCEPTION);
		} catch (IOException error) {
			System.out.println(IO_EXCEPTION);
			System.err.println(IOEXCEPTION_ERROR_MSG);
		}
	}
}

