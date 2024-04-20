package com.freud.ms.exception;

public class ConfigurationLoadException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfigurationLoadException() {
	}

	public ConfigurationLoadException(String message) {
		super(message);
	}

	public ConfigurationLoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationLoadException(Throwable cause) {
		super(cause);
	}
}
