package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

public class SJType implements SJInterface {
	@Override public void register(SRootClosure root) {
		root.bind("type", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(new SString(stack.pop().getType()));
			}
		}));
		root.bind("retype", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject newType = stack.pop();
				SObject obj = stack.pop();
				if (newType instanceof SString) {
					obj = obj.shallowCopy();
					obj.retype(((SString) newType).getString());
					stack.push(obj);
				}
			}
		}));
	}
}
