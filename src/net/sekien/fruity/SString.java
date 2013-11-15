package net.sekien.fruity;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 1:08 PM To change this template use File | Settings |
 * File Templates.
 */
public class SString extends SObject {
private final String value;

public SString(String value) {
	this.value=value;
}

public String getString() {
	return value;
}

@Override public String toString() {
	return "\""+value+"\"";
}

@Override public String toBasicString() {
	return value;
}


	@Override public String getType() {
		return "str";
	}
}
