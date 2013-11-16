package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

public class SJComparator implements SJInterface {
	@Override public void register(SRootClosure root) {
		root.bind("eq", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject b = stack.pop();
				SObject a = stack.pop();
				if (a.getType().equals(b.getType()) && a.toString().equals(b.toString()))
					stack.push(new SBool(true));
				else
					stack.push(new SBool(false));
			}
		}));
		root.bind("not", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject object = stack.pop();
				if (object instanceof SBool) {
					stack.push(new SBool(!((SBool) object).getBoolean()));
				} else {
					System.err.println("not arg should be bool");
				}
			}
		}));
		root.bind("lt", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject b = stack.pop();
				SObject a = stack.pop();
				if (a instanceof SInteger) {
					if (b instanceof SInteger) {
						stack.push(new SBool(((SInteger) a).getInt() < ((SInteger) b).getInt()));
					} else {
						System.err.println("lt arg 2 should be int");
					}
				} else {
					System.err.println("lt arg 1 should be int");
				}
			}
		}));
	}
}
