package net.sekien.fruity;

import java.util.*;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 12:47 PM To change this template use File | Settings |
 * File Templates.
 */
public class SClosure extends SObject {
	protected HashMap<String, SObject> variables;
	protected SClosure parent;
	protected String[] tokens;
	private String type = "closure";

	private SClosure resolved_context;
	private String resolved_name;
	private boolean resolve_test = false;

	public SClosure(SClosure parent, String[] code) {
		this.parent = parent;
		this.tokens = code;
		this.variables = new HashMap<String, SObject>();
	}

	public void exec(Stack<SObject> stack, Stack<SClosure> callStack) {
		Stack<Stack<SObject>> stackStack = new Stack<Stack<SObject>>();
		stackStack.push(stack);
		callStack.push(this);
		Stack<String> popFuncStack = new Stack<String>();

		int t = 0;
		while (t < tokens.length) try {
			String token = tokens[t++];
			resolve_test = true;
			resolveContextAndName(token);
			resolve_test = false;
			SObject possibleMatch = resolved_context.getVariable(resolved_name);
			if (possibleMatch != null) {
				if (possibleMatch instanceof SClosure) {
					try {
						((SClosure) possibleMatch.shallowCopy()).exec(stack, callStack);
					} catch (StackOverflowError e) {
						throw new SException("Stack overflow");
					}
				} else {
					stack.push(possibleMatch);
				}
			} else if (token.startsWith("$")) {
				resolveContextAndName(token.substring(1));
				SObject value = resolved_context.getVariable(resolved_name);
				if (value == null) {
					throw new SException("Error: can not get unbound var "+token.substring(1));
				} else {
					stack.push(value);
				}
			} else if (token.startsWith(">>")) {
				resolveContextAndName(token.substring(2));
				resolved_context.variables.put(resolved_name, stack.pop());
			} else if (token.startsWith(">")) {
				resolveContextAndName(token.substring(1));
				resolved_context.setVariable(resolved_name, stack.pop());
			} else if (token.matches("[0-9]+")) {
				stack.push(new SInteger(token));
			} else if (token.equals("(")) {
				stack = new Stack<SObject>();
				stackStack.push(stack);
				popFuncStack.push(null);
			} else if (token.equals(")")) {
				String popFunc;
				try {
					popFunc = popFuncStack.pop();
				} catch (EmptyStackException e) {
					throw new SException("unmatched ')'");
				}
				if (popFunc != null) {
					resolveContextAndName(popFunc);
					SObject possibleMatch2 = resolved_context.getVariable(resolved_name);
					if (possibleMatch2 != null && possibleMatch2 instanceof SClosure) {
						((SClosure) possibleMatch2).exec(stack, callStack);
					} else {
						throw new SException("no function bound to "+popFunc);
					}
				}
				stackStack.pop();
				stackStack.peek().addAll(stack);
				stack = stackStack.peek();
			} else if (token.endsWith("(")) {
				stack = new Stack<SObject>();
				stackStack.push(stack);
				popFuncStack.push(token.substring(0, token.length()-1));
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
			} else if (token.endsWith("{")) {
				boolean anon = false;
				if (token.equals("{")) anon = true;
				try {
					ArrayList<String> buildClosure = new ArrayList<String>();
					String tok;
					int lvl = 0;
					while (true) {
						tok = tokens[t++];
						if (tok.equals("}")) {
							if (lvl == 0) {
								break;
							} else {
								lvl--;
								buildClosure.add(tok);
							}
						} else {
							if (tok.endsWith("{")) lvl++;
							buildClosure.add(tok);
						}
					}
					String[] arr = new String[buildClosure.size()];
					buildClosure.toArray(arr);
					SClosure closure = new SClosure(this, arr);
					if (anon)
						stack.push(closure);
					else {
						closure.exec(stack, callStack);
						bindVariable(token.substring(0, token.length()-1), closure);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new SException("syntax error: unmatched brace");
				}
			} else {
				throw new SException("no function bound to "+token);
			}
		} catch (EmptyStackException e) {
			throw new SException("stack underflow");
		}
		callStack.pop();
	}

	private void resolveContextAndName(String reference) {
		String[] parts = (reference.startsWith(":")?reference.substring(1):reference).split(":");
		resolved_name = parts[parts.length-1];
		resolved_context = this;
		for (int i = 0; i < parts.length-1; i++) {
			SObject tmp = resolved_context.getVariable(parts[i]);
			if (tmp != null && tmp instanceof SClosure) {
				resolved_context = (SClosure) tmp;
			} else {
				if (resolve_test) {
					resolved_name = null;
					return;
				} else {
					throw new SException("reference '"+reference+"' contains non-existent context "+parts[i]);
				}
			}
		}
	}

	public SObject getVariable(String key) {
		SObject ret = variables.get(key);
		if (ret == null) {
			return parent.getVariable(key);
		} else {
			return ret;
		}
	}

	public void setVariable(String key, SObject value) {
		if (variables.containsKey(key)) {
			variables.put(key, value);
		} else {
			parent.setVariable(key, value);
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
			if (first) first = false;
			else builder.append(' ');
			builder.append(token);
		}
		builder.append('}');
		return builder.toString();
	}

	@Override public String toBasicString() {
		return toString();
	}

	@Override public String getType() {
		return type;
	}

	@Override public void retype(String type) {
		this.type = type;
	}

	@Override public SObject shallowCopy() {
		SClosure newobj = new SClosure(this.parent, tokens);
		newobj.variables = (HashMap<String, SObject>) this.variables.clone();
		return newobj;
	}

	@Override public SObject deepCopy() {
		SClosure newobj = new SClosure(this.parent, tokens);
		for (Map.Entry<String, SObject> var : variables.entrySet()) {
			newobj.variables.put(var.getKey(), var.getValue().deepCopy());
		}
		return newobj;
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

	public String getFullName() {
		if (parent == null || parent instanceof SRootClosure) {
			return getName();
		} else {
			return parent.getFullName()+":"+getName();
		}
	}

	public String getName() {
		if (parent == null) {
			return "<anon>";
		}
		for (Map.Entry<String, SObject> var : parent.variables.entrySet()) {
			if (var.getValue().equals(this)) return var.getKey();
		}
		return "<anon>";
	}

	public void setParent(SClosure parent) {
		this.parent = parent;
	}

	public HashMap<String, SObject> getVariables() {
		return variables;
	}

	public void copyVariablesOf(SClosure closure) {
		this.variables = closure.variables;
	}
}
