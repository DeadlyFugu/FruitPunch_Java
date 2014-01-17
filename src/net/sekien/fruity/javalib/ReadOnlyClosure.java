package net.sekien.fruity.javalib;

import net.sekien.fruity.SClosure;
import net.sekien.fruity.SException;
import net.sekien.fruity.SObject;

public class ReadOnlyClosure extends SClosure {
	public ReadOnlyClosure(SClosure parent, SClosure base) {
		super(parent, base.getTokens());
		this.copyVariablesOf(base);
	}

	@Override public void setVariable(String key, SObject value) {
		throw new SException("attempted to write "+key+" from locked closure "+getFullName());
	}
}
