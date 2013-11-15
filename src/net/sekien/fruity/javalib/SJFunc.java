package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 4/10/13 Time: 4:27 PM To change this template use File | Settings | File
 * Templates.
 */
public class SJFunc implements SJInterface {

@Override public void register(SRootClosure root) {
	root.bind("exec",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			SObject obj= stack.pop();
			if (obj instanceof SClosure) {
				((SClosure) obj).exec(stack, callStack);
			} else {
				System.out.println("Error: can only exec on a closure");
			}
		}
	}));
	root.bind("lambda",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			SObject closure = stack.pop();
			SObject args = stack.pop();
			if (closure instanceof SClosure) {
				if (args instanceof SString) {
					((SClosure) closure).exec(stack, callStack);
				} else {
					System.out.println("Error: can only lambda on a closure");
				}
			} else {
				System.out.println("Error: can only lambda on a closure");
			}
		}
	}));
	root.bind("disasm",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			SObject obj= stack.pop();
			if (obj instanceof SClosure) {
				String[] tokens = ((SClosure) obj).getTokens();
				for (String token : tokens)
					stack.push(new SString(token));
			} else {
				System.out.println("Error: can only disasm on a closure");
			}
		}
	}));
	root.bind("asm",new SJavaClosure(root,new JavaFunction() {
		@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
			String[] code = new String[stack.size()];
			int iter = 0;
			for (SObject s : stack) {
				if (s instanceof SString) {
					code[iter++] = ((SString) s).getString();
				} else {
					System.out.println("Error: asm on a non-string token");
				}
			}
			stack.clear();
			stack.push(new SClosure(callStack.peek(),code));
		}
	}));
}
}
