package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

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
	}
}
