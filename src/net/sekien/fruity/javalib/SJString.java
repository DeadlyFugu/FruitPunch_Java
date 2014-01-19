package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SJString implements SJInterface {
	@Override public void register(SRootClosure root) {
		root.bind("strcat2", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject b = stack.pop();
				SObject a = stack.pop();
				if (a instanceof SString) {
					if (b instanceof SString) {
						stack.push(new SString(((SString) a).getString().concat(((SString) b).getString())));
					} else {
						throw new SException("strcat2 arg 2 should be string");
					}
				} else {
					throw new SException("strcat2 arg 1 should be string");
				}
			}
		}));
		root.bind("tostr", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject a = stack.pop();
				stack.push(new SString(a.toBasicString()));
			}
		}));
		root.bind("toint", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject a = stack.pop();
				if (a instanceof SString) {
					stack.push(new SInteger(((SString) a).getString()));
				}
			}
		}));
		root.bind("splitOnFirst", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject b = stack.pop();
				SObject a = stack.pop();
				if (a instanceof SString) {
					if (b instanceof SString) {
						String[] split = ((SString) a).getString().split(((SString) b).getString(), 2);
						if (split.length == 1) {
							stack.push(a);
							stack.push(new SString(""));
						} else {
							stack.push(new SString(split[0]));
							stack.push(new SString(split[1]));
						}
					} else {
						throw new SException("splitOnFirst arg 2 should be string");
					}
				} else {
					throw new SException("splitOnFirst arg 1 should be string");
				}
			}
		}));
		root.bind("substring", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject end = stack.pop();
				SObject start = stack.pop();
				SObject a = stack.pop();
				if (start instanceof SInteger) {
					if (end instanceof SInteger) {
						if (a instanceof SString) {
							stack.push(new SString(((SString) a).getString().substring(
							                                                          ((SInteger) start).getInt(),
							                                                          ((SInteger) end).getInt())));
						}
					}
				}
			}
		}));
		root.bind("strlen", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject a = stack.pop();
				if (a instanceof SString) {
					stack.push(new SInteger(((SString) a).getString().length()));
				} else {
					throw new SException("strlen arg 1 should be string");
				}
			}
		}));
		root.bind("getchar", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject index = stack.pop();
				SObject a = stack.pop();
				if (index instanceof SInteger) {
					if (a instanceof SString) {
						stack.push(new SInteger(((SString) a).getString().charAt(((SInteger) index).getInt())));
					} else {
						throw new SException("getchar arg 1 should be string");
					}
				} else {
					throw new SException("getchar arg 2 should be int");
				}
			}
		}));
		root.bind("strchrlist", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject a = stack.pop();
				if (a instanceof SString) {
					byte[] bytes = ((SString) a).getString().getBytes();
					List<SObject> chars = new ArrayList<SObject>(bytes.length);
					for (byte b : bytes) {
						chars.add(new SInteger(b));
					}
					stack.push(new SList(chars));
				} else {
					throw new SException("strchrlist arg 1 should be string");
				}
			}
		}));
		root.bind("strfromlist", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject a = stack.pop();
				if (a instanceof SList) {
					byte[] bytes = new byte[((SList) a).size()];
					int i = 0;
					for (SObject obj : ((SList) a)) {
						if (obj instanceof SInteger) {
							bytes[i++] = (byte) ((SInteger) obj).getInt();
						} else {
							throw new SException("strfromlist received non-integer in list");
						}
					}
					stack.push(new SString(new String(bytes)));
				} else {
					throw new SException("strfromlist arg 1 should be list");
				}
			}
		}));
	}
}
