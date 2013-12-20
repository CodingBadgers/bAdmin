package uk.codingbadgers.badmin.exception;

@SuppressWarnings("serial")
public class ConfigException extends AdminException {

	public ConfigException(Throwable e) {
		super(e);
	}

	public ConfigException(String e) {
		super(e);
	}
}
