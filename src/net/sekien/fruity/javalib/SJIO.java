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
							throw new SException("Error: File not found for _rf: "+((SString) obj).getString()+" (expected at "+f+")");
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
				String file = stack.pop().toBasicString();
				int indexOfFinalSeparator = file.lastIndexOf('/');
				filePrefix.push(file.substring(0, indexOfFinalSeparator == -1?0:indexOfFinalSeparator));
				stack.push(new SString(file.substring(indexOfFinalSeparator == -1?0:indexOfFinalSeparator+1)));
			}
		}));
		root.bind("__rf_pop", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				if (filePrefix.isEmpty()) throw new SException("__rf_pop without matching __rf_push");
				filePrefix.pop();
			}
		}));
	}

	public String getFilePrefix() {
		StringBuilder sb = new StringBuilder("/");
		for (int i = filePrefix.size()-1; i >= 0; i--) {
			String s = filePrefix.get(i);
			if (s.equals("#BLANK#")) break;
			if (!s.equals("")) {
				sb.insert(0, s);
				sb.insert(0, File.separatorChar);
			}
		}
		return sb.toString();
	}
}
