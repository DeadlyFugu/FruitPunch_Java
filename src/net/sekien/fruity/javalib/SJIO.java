package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.io.*;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 4/10/13 Time: 4:29 PM To change this template use File | Settings | File
 * Templates.
 */
public class SJIO implements SJInterface {
	private static BufferedReader bufferedIn = new BufferedReader(new InputStreamReader(System.in));
	private String StringBuilder;
	private Stack<String> filePrefix = new Stack<String>();

	@Override public void register(SRootClosure root) {
		root.bind("print", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				System.out.println(stack.pop().toBasicString());
			}
		}));
		root.bind("printw", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				System.out.print(stack.pop().toBasicString());
			}
		}));
		root.bind("read", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				try {
					stack.push(new SString(bufferedIn.readLine()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
		root.bind("__rf", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				try {
					Object obj = stack.pop();
					if (obj instanceof SString) {
						File f = new File(((SString) obj).getString());
						if (!f.exists()) {
							f = new File(getFilePrefix()+((SString) obj).getString());
						}
						if (!f.exists()) {
							System.err.println("Error: File not found for _rf: "+((SString) obj).getString());
						}
						BufferedReader reader = new BufferedReader(new FileReader(f));
						String line;
						StringBuilder sb = new StringBuilder();
						while ((line = reader.readLine()) != null) {
							sb.append(line);
							sb.append('\n');
						}
						stack.push(new SClosure(callStack.peek(), StackScriptParser.parse(sb.toString())));
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
		root.bind("__rf_push", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				String file = stack.peek().toBasicString();
				filePrefix.push(file.substring(0, file.lastIndexOf('/') == -1?file.lastIndexOf('/'):file.length()));
			}
		}));
		root.bind("__rf_pop", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				filePrefix.pop();
			}
		}));
	}

	public String getFilePrefix() {
		StringBuilder sb = new StringBuilder();
		for (String s : filePrefix) {
			sb.append(s);
			sb.append(File.separatorChar);
		}
		return sb.toString();
	}
}
