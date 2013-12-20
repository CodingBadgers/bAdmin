package uk.codingbadgers.badmin.exception;

import java.sql.SQLException;

@SuppressWarnings("serial")
public class DatabaseException extends AdminException {

	public DatabaseException(Throwable cause) {
		super(cause);
	}

	public DatabaseException(String string) {
		super(string);
	}

	public DatabaseException(String string, SQLException e) { 
		super (string, e);
	}
}
