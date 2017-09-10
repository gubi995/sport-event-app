package hu.szeged.sporteventapp.common.exception;

public class AlreadyExistsException extends Exception {
	public AlreadyExistsException() {
	}

	public AlreadyExistsException(String message) {
		super(message);
	}

	public AlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public AlreadyExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
