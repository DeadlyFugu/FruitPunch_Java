import net.sekien.fruity.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.Stack;

public class FGL_Display {
	public static void register(SClosure root) {
		root.bindVariable("display_create", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				try {
					Display.create();
				} catch (LWJGLException e) {
					throw new SException("gl:display:create "+e.getMessage());
				}
			}
		}));
		root.bindVariable("display_setsize", new SJavaClosure(root, new JavaFunction() {
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
		root.bindVariable("display_isopen", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(new SBool(!Display.isCloseRequested()));
			}
		}));
		root.bindVariable("display_update", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				Display.update();
			}
		}));
		root.bindVariable("display_destroy", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				Display.destroy();
			}
		}));
	}
}
