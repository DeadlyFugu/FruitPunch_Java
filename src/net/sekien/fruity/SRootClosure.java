package net.sekien.fruity;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 1:13 PM To change this template use File | Settings |
 * File Templates.
 */
public class SRootClosure extends SClosure {
	public SRootClosure() {
		super(null, null);
		bindVariable("true", new SBool(true));
		bindVariable("false", new SBool(false));
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
			variables.put(key, value);
		} else {
			throw new SException("can not set unbound var "+key);
		}
	}

	public void eval(String code, Stack<SObject> stack) {
		this.tokens = StackScriptParser.parse(code);
		Stack<SClosure> callStack = new Stack<SClosure>();
		Stack<String> traceStack = new Stack<String>();
		traceStack.push("<root>");
		try {
			exec(stack, callStack);
		} catch (SException e) {
			System.out.println(e.getMessage());
			for (SClosure callPoint : callStack) {
				String codeHint = callPoint.toBasicString();
				if (codeHint.length() > 42) {
					codeHint = codeHint.substring(0, 38).concat("...");
				}
				System.out.println(" @"+callPoint.getFullName()+"    "+codeHint);
			}
		}
		traceStack.pop();
	}

	public void bind(String key, SObject value) {
		variables.put(key, value);
	}

	@Override public String toString() {
		return "<root>";
	}

	@Override public String toBasicString() {
		return toString();
	}

	public SClosure getParent() {
		throw new SException("'parent' on root closure");
	}

	@Override public String getName() {
		return "<root>";
	}
}
