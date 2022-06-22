package com.smd.bookstore.domainvalue;

public enum BookType {

	COMIC(10), FICTION(5), SCIENCE(7), HISTORY(20);

	private final int discount;

	BookType(int discount) {
		this.discount = discount;
	}

	public int getDiscount() {
		return discount;
	}

}