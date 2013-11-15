package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 4/10/13 Time: 4:16 PM To change this template use File | Settings | File
 * Templates.
 */
public class SJMath implements SJInterface {
public void register(SRootClosure root) {
	root.bind("+",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			SObject b = stack.pop();
			SObject a = stack.pop();
			if (a instanceof SInteger) {
				if (b instanceof SInteger) {
					stack.push(new SInteger(((SInteger) a).getInt()+((SInteger) b).getInt()));
				}
			}
		}
	}));
	root.bind("-",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			SObject b = stack.pop();
			SObject a = stack.pop();
			if (a instanceof SInteger) {
				if (b instanceof SInteger) {
					stack.push(new SInteger(((SInteger) a).getInt()-((SInteger) b).getInt()));
				}
			}
		}
	}));
	root.bind("*",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			SObject b = stack.pop();
			SObject a = stack.pop();
			if (a instanceof SInteger) {
				if (b instanceof SInteger) {
					stack.push(new SInteger(((SInteger) a).getInt()*((SInteger) b).getInt()));
				}
			}
		}
	}));
	root.bind("/",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			SObject b = stack.pop();
			SObject a = stack.pop();
			if (a instanceof SInteger) {
				if (b instanceof SInteger) {
					stack.push(new SInteger(((SInteger) a).getInt()/((SInteger) b).getInt()));
				}
			}
		}
	}));
}
}
