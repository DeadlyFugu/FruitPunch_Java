import net.sekien.fruity.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.Stack;

public class FGL_Input {
	public static void register(SClosure root) {
		root.bindVariable("mouse_down", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				int button = JavaHelper.getInt(stack, "mouse_down", 1);
				stack.push(new SBool(Mouse.isButtonDown(button)));
			}
		}));
		root.bindVariable("mouse_pos", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(new SInteger(Mouse.getX()));
				stack.push(new SInteger(Mouse.getY()));
			}
		}));
		root.bindVariable("kbd_down", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				int key = JavaHelper.getInt(stack, "kbd_down", 1);
				stack.push(new SBool(Keyboard.isKeyDown(key)));
			}
		}));
		root.bindVariable("kbd_next", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(new SBool(Keyboard.next()));
			}
		}));
		root.bindVariable("kbd_event_info", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(new SInteger(Keyboard.getEventKey()));
				stack.push(new SBool(Keyboard.getEventKeyState()));
				stack.push(new SInteger(Keyboard.getEventCharacter()));
				stack.push(new SLong(Keyboard.getEventNanoseconds()));
			}
		}));
	}
}
