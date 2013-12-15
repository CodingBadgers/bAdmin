package uk.codingbadgers.badmin.exception;

@SuppressWarnings("serial")
public class DatabaseException extends RuntimeException {

	public DatabaseException(Throwable cause) {
		super(cause);
	}

	public DatabaseException(String string) {
		super(string);
	}
}
