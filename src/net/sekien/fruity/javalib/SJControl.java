package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

public class SJControl implements SJInterface {
	@Override public void register(SRootClosure root) {
		root.bind("if", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject closure = stack.pop();
				SObject condition = stack.pop();
				if (closure instanceof SClosure) {
					if (condition instanceof SBool) {
						if (((SBool) condition).getBoolean())
							((SClosure) closure).exec(stack, callStack);
					} else {
						System.err.println("if arg 1 must be bool");
					}
				} else {
					System.err.println("if arg 2 must be closure");
				}
			}
		}));

		root.bind("ifelse", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject closureFalse = stack.pop();
				SObject closureTrue = stack.pop();
				SObject condition = stack.pop();
				if (closureFalse instanceof SClosure) {
					if (closureTrue instanceof SClosure) {
						if (condition instanceof SBool) {
							if (((SBool) condition).getBoolean())
								((SClosure) closureTrue).exec(stack, callStack);
							else
								((SClosure) closureFalse).exec(stack, callStack);
						} else {
							System.err.println("ifelse arg 1 must be bool");
						}
					} else {
						System.err.println("ifelse arg 2 must be closure");
					}
				} else {
					System.err.println("ifelse arg 3 must be closure");
				}
			}
		}));

		root.bind("while", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject closure = stack.pop();
				SObject condition = stack.pop();
				if (closure instanceof SClosure) {
					if (condition instanceof SClosure) {
						while (true) {
							int ssize = stack.size();
							((SClosure) condition).exec(stack, callStack);
							if (stack.size() != ssize+1) {
								System.err.println("while condition returned multiple values");
								break;
							}
							if (stack.peek() instanceof SBool) {
								if (((SBool) stack.pop()).getBoolean()) {
									((SClosure) closure).exec(stack, callStack);
								} else {
									break;
								}
							} else {
								System.err.println("while condition returned non-bool type");
							}
						}
					} else {
						System.err.println("while arg 1 must be closure");
					}
				} else {
					System.err.println("while arg 2 must be closure");
				}
			}
		}));
	}
}
