import net.sekien.fruity.*;

import java.util.Stack;

public class Main {
	public static void register(SRootClosure root) {
		SClosure namespace;
		namespace = (SClosure) root.getVariable("gl");
		if (namespace == null) {
			namespace = new SClosure(root, new String[0]);
			root.bindVariable("gl", namespace);
		}
		namespace.bindVariable("ver", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(new SInteger(0));
			}
		}));
		FGL_Display.register(namespace);
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir")+"/plugins/FruityGL/lib/native");
	}
}
