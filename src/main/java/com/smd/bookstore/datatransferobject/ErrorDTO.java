package com.smd.bookstore.datatransferobject;

public class ErrorDTO {
	private int code;
	private String description;

	public ErrorDTO(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
