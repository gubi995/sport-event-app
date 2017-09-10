package hu.szeged.sporteventapp.common.exception;

public class AlreadyJoinedException extends Exception {
	public AlreadyJoinedException() {
	}

	public AlreadyJoinedException(String message) {
		super(message);
	}

	public AlreadyJoinedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyJoinedException(Throwable cause) {
		super(cause);
	}

	public AlreadyJoinedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
