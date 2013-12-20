package uk.codingbadgers.badmin.exception;

import java.sql.SQLException;

@SuppressWarnings("serial")
public class AdminException extends RuntimeException {

	public AdminException(Throwable cause) {
		super(cause);
	}

	public AdminException(String string) {
		super(string);
	}

	public AdminException(String string, SQLException e) { 
		super (string, e);
	}
}
