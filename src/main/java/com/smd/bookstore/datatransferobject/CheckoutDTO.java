package com.smd.bookstore.datatransferobject;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CheckoutDTO {

	private String promotion;

	@NotEmpty(message = "ISBN can not be empty!")
	private List<String> isbn = new ArrayList<>();

	private Double price;

	private CheckoutDTO(String promotion, List<String> isbn) {
		super();
		this.promotion = promotion;
		this.isbn = isbn;
	}

	private CheckoutDTO(Double price) {
		super();
		this.price = price;
	}

	public String getPromotion() {
		return promotion;
	}

	public List<String> getIsbn() {
		return isbn;
	}

	public Double getPrice() {
		return price;
	}

	public static BookDTOBuilder newBuilder() {
		return new BookDTOBuilder();
	}

	public static class BookDTOBuilder {

		private String promotion;
		private List<String> isbn;
		private Double price;

		public BookDTOBuilder setPromotion(String promotion) {
			this.promotion = promotion;
			return this;
		}

		public BookDTOBuilder setIsbn(List<String> isbn) {
			this.isbn = isbn;
			return this;
		}

		public BookDTOBuilder setPrice(Double price) {
			this.price = price;
			return this;
		}

		public CheckoutDTO createCheckoutDTO() {
			return new CheckoutDTO(promotion, isbn);
		}

		public CheckoutDTO responseCheckoutDTO() {
			return new CheckoutDTO(price);
		}

	}

}
