package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

public class SJString implements SJInterface {
	@Override public void register(SRootClosure root) {
		root.bind("strcat", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				StringBuilder sb = new StringBuilder();
				//todo this
			}
		}));
	}
}
