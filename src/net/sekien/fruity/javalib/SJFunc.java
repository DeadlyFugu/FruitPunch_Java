package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 4/10/13 Time: 4:27 PM To change this template use File | Settings | File
 * Templates.
 */
public class SJFunc implements SJInterface {

	@Override public void register(SRootClosure root) {
		root.bind("exec", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject obj = stack.pop();
				if (obj instanceof SClosure) {
					((SClosure) obj.shallowCopy()).exec(stack, callStack);
				} else {
					throw new SException("can only exec on a closure");
				}
			}
		}));
		root.bind("execst", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject obj = stack.pop();
				if (obj instanceof SClosure) {
					((SClosure) obj).exec(stack, callStack);
					stack.push(obj);
				} else {
					throw new SException("can only execst on a closure");
				}
			}
		}));
		root.bind("lambda", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject closure = stack.pop();
				SObject args = stack.pop();
				if (closure instanceof SClosure) {
					if (args instanceof SString) {
						String[] arg_list = ((SString) args).getString().split(",");
						List<String> prepend_code = new ArrayList<String>(arg_list.length*3);
						for (int i = arg_list.length-1; i >= 0; i--) {
							String arg = arg_list[i];
							String name, type;
							if (arg.contains(":")) {
								String[] parts = arg.split(":", 2);
								name = parts[0];
								type = parts[1];
							} else {
								name = arg;
								type = null;
							}
							if (type != null) {
								prepend_code.add("\""+type);
								prepend_code.add("as:arg");
							}
							if (name.length() > 0) prepend_code.add(">>"+name);
						}
						String[] tokens = ((SClosure) closure).getTokens();
						String[] new_closure_tokens = new String[tokens.length+prepend_code.size()];
						int i = 0;
						for (; i < prepend_code.size(); )
							new_closure_tokens[i] = prepend_code.get(i++);
						for (int k = 0; k < tokens.length; )
							new_closure_tokens[i++] = tokens[k++];
						SClosure new_closure = new SClosure(parent, new_closure_tokens);
						new_closure.setParent(((SClosure) closure).getParent());
						new_closure.copyVariablesOf((SClosure) closure);
						stack.push(new_closure);
					} else {
						throw new SException("lambda arg 2 must be string");
					}
				} else {
					throw new SException("lambda arg 1 must be closure");
				}
			}
		}));
		root.bind("disasm", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject obj = stack.pop();
				if (obj instanceof SClosure) {
					String[] tokens = ((SClosure) obj).getTokens();
					for (String token : tokens)
						stack.push(new SString(token));
				} else {
					throw new SException("Error: can only disasm on a closure");
				}
			}
		}));
		root.bind("asm", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				String[] code = new String[stack.size()];
				int iter = 0;
				for (SObject s : stack) {
					if (s instanceof SString) {
						code[iter++] = ((SString) s).getString();
					} else {
						throw new SException("Error: asm on a non-string token");
					}
				}
				stack.clear();
				stack.push(new SClosure(callStack.peek(), code));
			}
		}));
		root.bind("parse", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject string = stack.pop();
				if (string instanceof SString) {
					for (String str : StackScriptParser.parse(((SString) string).getString())) {
						stack.push(new SString(str));
					}
				} else {
					throw new SException("parse arg is not a string");
				}
			}
		}));
	}
}
