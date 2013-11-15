package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

public class SJContext implements SJInterface {
	@Override public void register(SRootClosure root) {
		root.bind("this",new SJavaClosure(root,new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(callStack.peek());
			}
		}));
		root.bind("setv",new SJavaClosure(root,new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject closure = stack.pop();
				SObject value = stack.pop();
				SObject key = stack.pop();
				if (closure instanceof SClosure)
					((SClosure) closure).setVariable(key.toBasicString(),value);
				else
					System.err.println("setv arg 3 must be closure");
			}
		}));
		root.bind("bindv",new SJavaClosure(root,new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject closure = stack.pop();
				SObject value = stack.pop();
				SObject key = stack.pop();
				if (closure instanceof SClosure)
					((SClosure) closure).bindVariable(key.toBasicString(),value);
				else
					System.err.println("setv arg 3 must be closure");
			}
		}));
		root.bind("getv",new SJavaClosure(root,new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject closure = stack.pop();
				SObject key = stack.pop();
				if (closure instanceof SClosure)
					((SClosure) closure).getVariable(key.toBasicString());
				else
					System.err.println("getv arg 2 must be closure");
			}
		}));
		root.bind("parent",new SJavaClosure(root,new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject closure = stack.pop();
				if (closure instanceof SClosure)
					stack.push(((SClosure) closure).getParent());
				else
					System.err.println("parent arg must be closure");
			}
		}));
		root.bind("lsv",new SJavaClosure(root,new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject closure = stack.pop();
				if (closure instanceof SClosure)
					stack.push(new SList(((SClosure) closure).listVariables()));
				else
					System.err.println("lsv arg must be closure");
			}
		}));
		root.bind("caller",new SJavaClosure(root,new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(callStack.get(callStack.size()-2));
			}
		}));
		root.bind("__in_jtype",new SJavaClosure(root,new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(new SString(stack.pop().getClass().getName()));
			}
		}));
	}
}
