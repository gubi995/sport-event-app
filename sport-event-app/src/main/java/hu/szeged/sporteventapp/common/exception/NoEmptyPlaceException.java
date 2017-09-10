package hu.szeged.sporteventapp.common.exception;

public class NoEmptyPlaceException extends Exception {
	public NoEmptyPlaceException() {
	}

	public NoEmptyPlaceException(String message) {
		super(message);
	}

	public NoEmptyPlaceException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoEmptyPlaceException(Throwable cause) {
		super(cause);
	}

	public NoEmptyPlaceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
