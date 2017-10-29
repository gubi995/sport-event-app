package hu.szeged.sporteventapp.common.exception;

public class NotAllowedFileTypeException extends Exception {

	public NotAllowedFileTypeException() {
		super();
	}

	public NotAllowedFileTypeException(String message) {
		super(message);
	}

	public NotAllowedFileTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAllowedFileTypeException(Throwable cause) {
		super(cause);
	}

	protected NotAllowedFileTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
