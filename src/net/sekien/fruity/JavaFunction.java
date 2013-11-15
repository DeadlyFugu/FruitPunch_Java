package net.sekien.fruity;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 1:43 PM To change this template use File | Settings |
 * File Templates.
 */
public interface JavaFunction {
public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent);
}
