package net.sekien.fruity.javalib;

import net.sekien.fruity.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Stack;

public class SJSystem implements SJInterface {
	@Override public void register(SRootClosure root) {
		final SRootClosure _root = root;
		root.bind("loadjar", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				SObject plugin_name = stack.pop();
				if (!(plugin_name instanceof SString)) {
					throw new SException("loadjar arg 1 must be string");
				}
				File file = new File(((SString) plugin_name).getString());
				try {
					URL[] urls = new URL[]{file.toURI().toURL()};
					ClassLoader loader = new URLClassLoader(urls);
					Class c = loader.loadClass("Main");
					c.getMethod("register", SRootClosure.class).invoke(null, _root);
				} catch (ClassNotFoundException e) {
					throw new SException("loadjar couldn't load "+plugin_name+" (missing Main class)");
				} catch (NoSuchMethodException e) {
					throw new SException("loadjar couldn't load "+plugin_name+" (missing register(SRootClosure) method)");
				} catch (InvocationTargetException e) {
					throw new SException("loadjar loading "+plugin_name+" caught "+e);
				} catch (MalformedURLException e) {
					throw new SException("loadjar url "+plugin_name+" malformed");
				} catch (IllegalAccessException e) {
					throw new SException("loadjar couldn't load "+plugin_name+" (illegal access exception "+e.getMessage());
				}
			}
		}));
		root.bind("time", new SJavaClosure(root, new JavaFunction() {
			@Override public void onCall(Stack<SClosure> callStack, Stack<SObject> stack, SClosure parent) {
				stack.push(new SInteger((int) System.currentTimeMillis()));
			}
		}));
	}
}
