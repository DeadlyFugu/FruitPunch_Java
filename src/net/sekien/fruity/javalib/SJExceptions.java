package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

public class SJExceptions implements SJInterface {
	@Override public void register(SRootClosure root) {
		root.bind("throw", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject a = stack.pop();
				if (a instanceof SString) {
					throw new SException(((SString) a).getString());
				} else {
					throw new SException("strcat2 arg 1 should be string");
				}
			}
		}));
	}
}