package hu.szeged.sporteventapp.common.exception;

public class NotParticipantException extends Exception {
	public NotParticipantException() {
	}

	public NotParticipantException(String message) {
		super(message);
	}

	public NotParticipantException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotParticipantException(Throwable cause) {
		super(cause);
	}

	public NotParticipantException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
