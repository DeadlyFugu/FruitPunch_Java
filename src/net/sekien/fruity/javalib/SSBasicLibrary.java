package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 1:35 PM To change this template use File | Settings |
 * File Templates.
 */
public class SSBasicLibrary {
	public static void registerWith(SRootClosure real_root) {
		SRootClosure root = new SRootClosure();
		new SJStack().register(root);
		new SJMath().register(root);
		new SJFunc().register(root);
		new SJIO().register(root);
		new SJList().register(root);
		new SJType().register(root);
		new SJContext().register(root);
		new SJControl().register(root);
		new SJComparator().register(root);
		new SJString().register(root);
		new SJExceptions().register(root);
		new SJSystem().register(root);

		root.bind("StackClear", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.clear();
			}
		}));

		ReadOnlyClosure native_closure = new ReadOnlyClosure(real_root, root);
		real_root.bind("native", native_closure);
	}
}
