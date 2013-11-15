package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

public class SJType implements SJInterface {
	@Override public void register(SRootClosure root) {
		root.bind("type",new SJavaClosure(root,new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(new SString(stack.pop().getType()));
			}
		}));
	}
}
