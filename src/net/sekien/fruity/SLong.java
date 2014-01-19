package net.sekien.fruity;

public class SLong extends SObject {
	private final long value;

	public SLong(long value) {
		this.value = value;
	}

	public long getLong() {
		return value;
	}

	@Override public String toString() {
		return Long.toString(value).concat(":long");
	}

	@Override public String toBasicString() {
		return Long.toString(value);
	}

	@Override public String getType() {
		return "long";
	}

	@Override public void retype(String type) {
		throw new SException("cannot retype long");
	}

	@Override public SObject shallowCopy() {
		return new SLong(value);
	}
}
