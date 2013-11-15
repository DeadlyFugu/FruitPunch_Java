package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 4/10/13 Time: 4:24 PM To change this template use File | Settings | File
 * Templates.
 */
public class SJStack implements SJInterface {
@Override public void register(SRootClosure root) {
	root.bind("dup",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			stack.push(stack.peek());
		}
	}));
	root.bind("pop",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			stack.pop();
		}
	}));
	root.bind("swap",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			SObject b = stack.pop();
			SObject a = stack.pop();
			stack.push(b);
			stack.push(a);
		}
	}));
}
}
