package com.test.exceptions;

public class ProvidePathException extends Exception {

	private static final long serialVersionUID = 1L;

		public ProvidePathException() {
			super("ProvidePathException");
		}
		public ProvidePathException(String msg) {
			super(msg);
		}
}
