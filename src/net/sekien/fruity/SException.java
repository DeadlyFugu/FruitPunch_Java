package net.sekien.fruity;

public class SException extends RuntimeException {
	private final String message;

	public SException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
