package net.sekien.fruity;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 1:37 PM To change this template use File | Settings |
 * File Templates.
 */
public class SJavaClosure extends SClosure {
private final JavaFunction function;

public SJavaClosure(SClosure parent, JavaFunction function) {
	super(parent, null);
	this.function = function;
}

@Override public SException exec(Stack<SObject> stack, Stack<SClosure> callStack) {
	try {
		function.onCall(callStack, stack, this);
	} catch (EmptyStackException e) {
		System.out.println("Error: native call requires more arguments");
	}
	return null;
}

@Override public String toString() {
	return "<native>";
}

@Override public String toBasicString() {
	return toString();
}
}
