package com.smd.bookstore.exception;

public class DuplicateEntityException extends Exception {
	private static final long serialVersionUID = -1450920218080463925L;

	public DuplicateEntityException(String message) {
		super(message);
	}

}
