package net.sekien.fruity;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 1:04 PM To change this template use File | Settings |
 * File Templates.
 */
public class SInteger extends SObject {
private final int value;

	public SInteger(int value) {
	this.value = value;
}

public SInteger(String value) {
	this(Integer.valueOf(value));
}

public int getInt() {
	return value;
}

@Override public String toString() {
	return String.valueOf(value).concat(":int");
}

@Override public String toBasicString() {
	return String.valueOf(value);
}

	@Override public String getType() {
		return "int";
	}
}
