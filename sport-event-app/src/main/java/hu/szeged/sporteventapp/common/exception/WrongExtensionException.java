package hu.szeged.sporteventapp.common.exception;

public class WrongExtensionException extends Exception {

	public WrongExtensionException() {
	}

	public WrongExtensionException(String message) {
		super(message);
	}

	public WrongExtensionException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrongExtensionException(Throwable cause) {
		super(cause);
	}

	public WrongExtensionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
