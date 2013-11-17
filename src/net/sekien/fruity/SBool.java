package net.sekien.fruity;

public class SBool extends SObject {
	private final boolean value;

	public SBool(boolean value) {
		this.value = value;
	}

	public SBool(String value) {
		this(Boolean.valueOf(value));
	}

	public boolean getBoolean() {
		return value;
	}

	@Override public String toString() {
		return String.valueOf(value);
	}

	@Override public String toBasicString() {
		return String.valueOf(value);
	}

	@Override public String getType() {
		return "bool";
	}

	@Override public void retype(String type) {
		throw new SException("cannot retype bool");
	}

	@Override public SObject shallowCopy() {
		return new SBool(value);
	}
}
