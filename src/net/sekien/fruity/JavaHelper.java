package net.sekien.fruity;

import java.util.Stack;

public class JavaHelper {
	public static int getInt(Stack<SObject> stack, String fname, int argid) {
		SObject obj = stack.pop();
		if (obj instanceof SInteger) {
			return ((SInteger) obj).getInt();
		} else {
			throw new SException(fname+" arg "+argid+" must be int, got "+obj.getType()+" (value: "+obj.toString()+") instead");
		}
	}
}
