import net.sekien.fruity.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.Stack;

public class FGL_Input {
	public static void register(SClosure root) {
		SClosure namespace = new SClosure(root, new String[0]);
		root.bindVariable("display", namespace);

		namespace.bindVariable("create", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				try {
					Display.create();
				} catch (LWJGLException e) {
					throw new SException("gl:display:create "+e.getMessage());
				}
			}
		}));
		namespace.bindVariable("setsize", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				try {
					int height = JavaHelper.getInt(stack, "setsize", 2);
					int width = JavaHelper.getInt(stack, "setsize", 1);
					Display.setDisplayMode(new DisplayMode(
					                                      width,
					                                      height));
				} catch (LWJGLException e) {
					throw new SException("gl:display:setsize "+e.getMessage());
				}
			}
		}));
		namespace.bindVariable("isopen", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(new SBool(!Display.isCloseRequested()));
			}
		}));
		namespace.bindVariable("update", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				Display.update();
			}
		}));
		namespace.bindVariable("destroy", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				Display.destroy();
			}
		}));
	}
}
