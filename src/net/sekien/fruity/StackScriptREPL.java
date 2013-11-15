package net.sekien.fruity;

import net.sekien.fruity.javalib.SSBasicLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 12:26 PM To change this template use File | Settings |
 * File Templates.
 */
public class StackScriptREPL {
public static void main(String[] args) throws IOException {
	int x;
	BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	SRootClosure root = new SRootClosure();
	Stack<SObject> stk = new Stack<SObject>();
	SSBasicLibrary.registerWith(root);
	root.eval("\"Hello, welcome to the repl!\\nHave fun! (type 'exit' to close)\" print",stk);
	while(true) {
		System.out.print("> ");
		String in = stdin.readLine();
		if (in.equals("exit")) break;
		root.eval(in, stk);
		System.out.println(stk);
	}
}
}
