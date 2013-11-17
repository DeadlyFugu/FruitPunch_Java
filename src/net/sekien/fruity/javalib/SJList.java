package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 4/10/13 Time: 5:33 PM To change this template use File | Settings | File
 * Templates.
 */
public class SJList implements SJInterface {
	@Override public void register(SRootClosure root) {
		root.bind("list", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SList list = new SList(stack);
				stack.clear();
				stack.push(list);
			}
		}));
		root.bind("open", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject obj = stack.pop();
				if (obj instanceof SList) {
					for (SObject li : ((SList) obj)) {
						stack.push(li);
					}
				} else {
					throw new SException("Error: can only open on a list");
				}
			}
		}));
	}
}
