package net.sekien.fruity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 12:47 PM To change this template use File | Settings |
 * File Templates.
 */
public class SClosure extends SObject {
protected HashMap<String,SObject> variables;
protected SClosure parent;
protected String[] tokens;

public SClosure(SClosure parent, String[] code) {
	this.parent = parent;
	this.tokens = code;
	this.variables = new HashMap<String, SObject>();
}

public SException exec(Stack<SObject> stack, Stack<SClosure> callStack) {
	Stack<Stack<SObject>> stackStack = new Stack<Stack<SObject>>();
	stackStack.push(stack);
	callStack.push(this);
	Stack<String> popFuncStack = new Stack<String>();

	int t = 0;
	while (t<tokens.length) {
		String token = tokens[t++];
		SObject possibleMatch = getVariable(token);
		if (possibleMatch!=null) {
			if (possibleMatch instanceof SClosure) {
				try {
					((SClosure) possibleMatch).exec(stack, callStack);
				} catch (StackOverflowError e) {
					return new SException("Stack overflow");
				}
			} else {
				stack.push(possibleMatch);
			}
		} else if (token.startsWith("$")) {
			SObject value = getVariable(token.substring(1));
			if (value==null) {
				System.out.println("Error: can not get unbound var "+token.substring(1));
			} else {
				stack.push(value);
			}
		} else if (token.startsWith(">>")) {
			variables.put(token.substring(2), stack.pop());
		} else if (token.startsWith(">")) {
			setVariable(token.substring(1), stack.pop());
		} else if (token.matches("[0-9]+")) {
			stack.push(new SInteger(token));
		} else if (token.equals("(")) {
			stack = new Stack<SObject>();
			stackStack.push(stack);
			popFuncStack.push(null);
		} else if (token.equals(")")) {
			stackStack.pop();
			stackStack.peek().addAll(stack);
			stack = stackStack.peek();
			String popFunc = popFuncStack.pop();
			SObject possibleMatch2 = getVariable(popFunc);
			if (possibleMatch2!=null && possibleMatch2 instanceof SClosure) {
				((SClosure) possibleMatch2).exec(stack,callStack);
			}
		} else if (token.endsWith("(")) {
			stack = new Stack<SObject>();
			stackStack.push(stack);
			popFuncStack.push(token.substring(0,token.length()-1));
		} else if (token.startsWith("\"")) {
			stack.push(new SString(token.substring(1)));
		} else if (token.matches("\\.+")) {
			stackStack.pop();
			Stack<SObject> second = stackStack.peek();
			int secondSize = second.size();
			stack.addAll(second.subList(secondSize-token.length(), secondSize));
			for (int i = 0; i < token.length(); i++) {
				second.pop();
			}
			stackStack.push(stack);
		} else if (token.startsWith("{")) {
			ArrayList<String> buildClosure = new ArrayList<String>();
			String tok;
			int lvl = 0;
			while (true) {
				tok = tokens[t++];
				if (tok.equals("}")) {
					if (lvl==0) {
						break;
					} else {
						lvl--;
						buildClosure.add(tok);
					}
				} else {
					if (tok.equals("{")) lvl++;
					buildClosure.add(tok);
				}
			}
			String[] arr = new String[buildClosure.size()];
			buildClosure.toArray(arr);
			stack.push(new SClosure(this,arr));
		} else {
			System.out.println("Error: No function bound to "+token);
		}
	}
	callStack.pop();
	return null;
}

public SObject getVariable(String key) {
	SObject ret = variables.get(key);
	if (ret==null) {
		return parent.getVariable(key);
	} else {
		return ret;
	}
}

public void setVariable(String key, SObject value) {
	if (variables.containsKey(key)) {
		variables.put(key,value);
	} else {
		parent.setVariable(key,value);
	}
}

	public void bindVariable(String key, SObject value) {
		variables.put(key, value);
	}

@Override public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append('{');
	boolean first = true;
	for (String token : tokens) {
		if (first) first=false; else builder.append(' ');
		builder.append(token);
	}
	builder.append('}');
	return builder.toString();
}

@Override public String toBasicString() {
	return toString();
}

	@Override public String getType() {
		return "closure";
	}

	public String[] getTokens() {
	return tokens;
}

	public SClosure getParent() {
		return parent;
	}

	public List<SObject> listVariables() {
		List<SObject> ret = new ArrayList<SObject>();
		for (String key : variables.keySet()) {
			ret.add(new SString(key));
		}
		return ret;
	}
}
