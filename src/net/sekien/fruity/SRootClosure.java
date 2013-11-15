package net.sekien.fruity;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 1:13 PM To change this template use File | Settings |
 * File Templates.
 */
public class SRootClosure extends SClosure {
public SRootClosure() {
	super(null,null);
	bindVariable("true",new SBool(true));
	bindVariable("false",new SBool(false));
}

@Override public SObject getVariable(String key) {
	if (variables.containsKey(key)) {
		return variables.get(key);
	} else {
		return null;
	}
}

@Override public void setVariable(String key, SObject value) {
	if (variables.containsKey(key)) {
		variables.put(key,value);
	} else {
		System.out.println("Error: can not set unbound var "+key);
	}
}

public void eval(String code, Stack<SObject> stack) {
	this.tokens=StackScriptParser.parse(code);
	Stack<SClosure> callStack = new Stack<SClosure>();
	callStack.push(this);
	exec(stack, new Stack<SClosure>());
}

public void bind(String key, SObject value) {
	variables.put(key,value);
}

@Override public String toString() {
	return "<root>";
}

@Override public String toBasicString() {
	return toString();
}

	public SClosure getParent() {
		System.err.println("warn: parent on root closure");
		return this;
	}
}
