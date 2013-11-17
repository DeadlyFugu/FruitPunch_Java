package net.sekien.fruity;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 12:47 PM To change this template use File | Settings |
 * File Templates.
 */
public abstract class SObject {
	public abstract String toString();

	public abstract String toBasicString();

	public abstract String getType();

	public abstract void retype(String type);

	public abstract SObject shallowCopy();

	public SObject deepCopy() {
		return shallowCopy();
	}
}
